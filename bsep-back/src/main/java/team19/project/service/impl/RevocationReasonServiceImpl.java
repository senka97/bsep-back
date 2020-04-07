package team19.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team19.project.dto.RevocationReasonDTO;
import team19.project.model.RevocationReason;
import team19.project.repository.RevocationReasonRepository;
import team19.project.service.RevocationReasonService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RevocationReasonServiceImpl implements RevocationReasonService {

    @Autowired
    private RevocationReasonRepository revocationReasonRepository;

    @Override
    public RevocationReason findOne(Long id) {
        return revocationReasonRepository.findById(id).orElse(null);
    }

    @Override
    public List<RevocationReasonDTO> findAll() {
        List<RevocationReason> revocationReason = revocationReasonRepository.findAll();
        List<RevocationReasonDTO> revocationReasonDTOS = new ArrayList<>();

        for (RevocationReason reason : revocationReason) {
            revocationReasonDTOS.add(new RevocationReasonDTO(reason));
        }

        return revocationReasonDTOS;
    }


}
