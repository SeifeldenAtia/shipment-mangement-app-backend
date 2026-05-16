package com.example.shipmentApp.shipment;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentDTO.ShipmentResponse createShipment(ShipmentDTO.CreateShipmentRequest request) {
        final String trackingNumber = generateTrackingNumber();
        final Shipment shipment = Shipment.builder()
                .trackingNumber(trackingNumber)
                .origin(request.origin)
                .destination(request.destination)
                .estimatedDelivery(request.estimatedDelivery)
                .build();

        shipmentRepository.save(shipment);

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
}
