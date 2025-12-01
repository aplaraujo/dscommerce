package com.example.dscommerce.controllers;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public interface GenericController {
    default URI gerarHeaderLocation(String id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}
