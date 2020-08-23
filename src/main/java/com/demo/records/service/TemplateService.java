package com.demo.records.service;

import com.demo.records.domain.TemplateDO;

import java.util.List;

public interface TemplateService {
    List<TemplateDO> getTemplatesInTeam(int teamId);

    int createTemplate(TemplateDO templateDO);

    TemplateDO findById(int id);

    List<TemplateDO> findByType(int teamId, String type);

    List<TemplateDO> getAllTemplates();

    int updateTemplate(TemplateDO templateDO);

    int deleteTemplate(int id);
}
