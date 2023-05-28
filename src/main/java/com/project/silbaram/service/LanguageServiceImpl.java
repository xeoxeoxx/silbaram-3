package com.project.silbaram.service;

import com.project.silbaram.dao.LanguageDAO;
import com.project.silbaram.dto.CategoryDTO;
import com.project.silbaram.dto.LanguageDTO;
import com.project.silbaram.vo.LanguageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final LanguageDAO languageDAO;
    private final ModelMapper modelMapper;

    @Override
    public LanguageDTO getLanguageById(Long lgid){
        LanguageVO languageVO = languageDAO.getLanguageById(lgid);
        log.info(languageVO);
        LanguageDTO languageDTO = modelMapper.map(languageVO, LanguageDTO.class);
        log.info(languageDTO);

        return languageDTO;
    }

    @Override
    public List<LanguageDTO> selectAll() {
        return languageDAO.selectAll();
    }
}
