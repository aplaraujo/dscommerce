package com.example.dscommerce.controllers.mappers;


import com.example.dscommerce.dto.ClientDTO;
import com.example.dscommerce.entities.Client;

public class ClientMapper {

    public static Client toEntity(ClientDTO dto) {
        Client client = new Client();
        client.setId(dto.id());
        client.setName(dto.name());
        return client;
    }

    public static ClientDTO toDTO(Client entity) {
        return new ClientDTO(
                entity.getId(),
                entity.getName()
        );
    }
}
