package br.com.vetequina.api.mapper;

import org.springframework.stereotype.Component;
import br.com.vetequina.api.dto.med.MedicalRecordCreateRequest;
import br.com.vetequina.api.dto.med.MedicalRecordUpdateRequest;
import br.com.vetequina.api.dto.med.MedicalRecordResponse;
import br.com.vetequina.api.entity.MedicalRecord;

@Component
public class MedicalRecordMapper {

    public MedicalRecord toEntity(MedicalRecordCreateRequest req) {
        if (req == null)
            return null;
        MedicalRecord mr = new MedicalRecord();
        mr.setDate(req.date());
        mr.setDescription(req.description());
        mr.setFileUrl(req.fileUrl());
        return mr;
    }

    public void update(MedicalRecord target, MedicalRecordUpdateRequest req) {
        if (target == null || req == null)
            return;
        if (req.date() != null)
            target.setDate(req.date());
        if (req.description() != null)
            target.setDescription(req.description());
        if (req.fileUrl() != null)
            target.setFileUrl(req.fileUrl());
    }

    public MedicalRecordResponse toResponse(MedicalRecord mr) {
        if (mr == null)
            return null;
        return new MedicalRecordResponse(
                mr.getId(),
                mr.getHorse() != null ? mr.getHorse().getId() : null,
                mr.getDate(),
                mr.getDescription(),
                mr.getFileUrl());
    }
}
