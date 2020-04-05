package team19.project.service;

import team19.project.model.RevocationReason;

import java.util.List;

public interface RevocationReasonService {

    RevocationReason findOne(Long id);
    List<RevocationReason> findAll();
}
