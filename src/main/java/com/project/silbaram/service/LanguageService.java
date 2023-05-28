package com.project.silbaram.service;

import com.project.silbaram.dto.CategoryDTO;
import com.project.silbaram.dto.LanguageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LanguageService {

    LanguageDTO getLanguageById(Long lgid);

    List<LanguageDTO> selectAll();
}
