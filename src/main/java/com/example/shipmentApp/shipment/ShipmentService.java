package com.example.shipmentApp.shipment;


import com.example.shipmentApp.shipment.error.ShipmentNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ShipmentDTO.ShipmentResponse createShipment(ShipmentDTO.CreateShipmentRequest request) {
        final String trackingNumber = generateTrackingNumber();
        final Shipment shipment = Shipment.builder()
                .trackingNumber(trackingNumber)
                .origin(request.origin)
                .destination(request.destination)
                .estimatedDelivery(request.estimatedDelivery)
                .build();

        shipmentRepository.save(shipment);
        notifyShipmentStatus(shipment, getStatusMessage(shipment.getStatus()));

        return mapToResponse(shipment);
    }

    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private ShipmentDTO.ShipmentResponse mapToResponse(Shipment shipment) {
        return ShipmentDTO.ShipmentResponse.builder()
                .id(shipment.getId())
                .trackingNumber(shipment.getTrackingNumber())
                .origin(shipment.getOrigin())
                .destination(shipment.getDestination())
                .status(shipment.getStatus())
                .createdAt(shipment.getCreatedAt())
                .updatedAt(shipment.getUpdatedAt())
                .currentLocation(shipment.getCurrentLocation())
                .estimatedDelivery(shipment.getEstimatedDelivery())
                .build();

    }

    public List<ShipmentDTO.ShipmentResponse> getAllShipments() {
        List<Shipment> shipments = shipmentRepository.findAll();
        return shipments.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ShipmentDTO.ShipmentResponse getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with id " + id));

        return mapToResponse(shipment);
    }

    public ShipmentDTO.ShipmentResponse getShipmentByTrackingNumber(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(
                        () -> new ShipmentNotFoundException("Shipment not found with trackingNumber " + trackingNumber)
                );

        return mapToResponse(shipment);
    }

    public ShipmentDTO.ShipmentResponse updateShipment(
            ShipmentDTO.UpdateShipmentRequest request, @PathVariable Long id) {

        var shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with id " + id));

        shipment.setStatus(request.getStatus());
        shipment.setCurrentLocation(request.getCurrentLocation());

        shipment = shipmentRepository.save(shipment);
        notifyShipmentStatus(shipment, getStatusMessage(shipment.getStatus()));

        return mapToResponse(shipment);
    }

    public void notifyShipmentStatus(Shipment shipment, String message) {
        var update = ShipmentDTO.StatusUpdateMessage.builder()
                .shipmentId(shipment.getId())
                .trackingNumber(shipment.getTrackingNumber())
                .status(shipment.getStatus())
                .currentLocation(shipment.getCurrentLocation())
                .timestamp(shipment.getUpdatedAt())
                .message(message)
                .build();

        messagingTemplate.convertAndSend("/topic/shipments", update);
        messagingTemplate.convertAndSend("/topic/shipments" + shipment.getId(), update);

        log.info("Sent shipment status update: {}", update);
    }

    private String getStatusMessage(ShipmentStatus status) {
        return switch (status) {
            case ORDER_PLACED -> "Order has been placed";
            case PROCESSING -> "Order is being processed";
            case PICKED_UP -> "Package has been picked up";
            case IN_TRANSIT -> "Package is in transit";
            case OUT_FOR_DELIVERY -> "Package is out for delivery";
            case DELIVERED -> "Package has been delivered";
            case EXCEPTION -> "Delivery exception occurred";
        };
    }
}
