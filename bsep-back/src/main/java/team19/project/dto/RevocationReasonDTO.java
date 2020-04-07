package team19.project.dto;

import team19.project.model.RevocationReason;

public class RevocationReasonDTO {

    private Long id;
    private String name;

    public RevocationReasonDTO() {
    }

    public RevocationReasonDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public RevocationReasonDTO(RevocationReason revocationReason) {
        this.id = revocationReason.getId();
        this.name = revocationReason.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
