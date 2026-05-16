package com.example.shipmentApp.shipment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    private ResponseEntity<ShipmentDTO.ShipmentResponse> createShipment
            (@Valid @RequestBody
             ShipmentDTO.CreateShipmentRequest createShipmentRequest
            ) {

        var shipmentResponse = shipmentService.createShipment(createShipmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentResponse);
    }

    @GetMapping
    private ResponseEntity<List<ShipmentDTO.ShipmentResponse>> getAllShipments() {

        var shipments = shipmentService.getAllShipments();
        return ResponseEntity.status(HttpStatus.OK).body(shipments);
    }
}
