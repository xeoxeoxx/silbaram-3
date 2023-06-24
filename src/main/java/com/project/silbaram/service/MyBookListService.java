package com.project.silbaram.service;

import com.project.silbaram.dto.OrderInfoDTO;
import com.project.silbaram.dto.PageRequestDTO;
import com.project.silbaram.dto.PageResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface MyBookListService {

    PageResponseDTO<OrderInfoDTO> getAllMyBooks(PageRequestDTO pageRequestDTO, Long memberId);



}
