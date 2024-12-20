package sn.groupeisi.ss.api.dto;

import lombok.Data;

@Data
public class EtudiantDto extends UserDto {
    private String niveau;
    private Long filiereId; // ID de la filière choisie
}
