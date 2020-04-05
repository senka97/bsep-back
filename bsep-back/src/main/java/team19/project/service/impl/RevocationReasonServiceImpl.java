package team19.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team19.project.model.RevocationReason;
import team19.project.repository.RevocationReasonRepository;
import team19.project.service.RevocationReasonService;

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
    public List<RevocationReason> findAll() {
        return revocationReasonRepository.findAll();
    }


}
