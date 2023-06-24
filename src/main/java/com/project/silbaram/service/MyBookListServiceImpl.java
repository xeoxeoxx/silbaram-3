package com.project.silbaram.service;

import com.project.silbaram.dao.MyBookListDAO;
import com.project.silbaram.dto.*;
import com.project.silbaram.vo.OrderInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MyBookListServiceImpl implements MyBookListService {

    private final MyBookListDAO myBookListDAO;
    private final ModelMapper modelMapper;

    @Override
    public PageResponseDTO<OrderInfoDTO> getAllMyBooks(PageRequestDTO pageRequestDTO, Long memberId) {
        List<OrderInfoVO> voList = myBookListDAO.selectAllMyBooks(pageRequestDTO, memberId);
        log.info(voList);
        List<OrderInfoDTO> dtoList = new ArrayList<>();
        for (OrderInfoVO orderInfoVO: voList
        ) {
            OrderInfoDTO dto =modelMapper.map(orderInfoVO, OrderInfoDTO.class); // vo를 dto로 mapping
//            dto.setBook(modelMapper.map(orderInfoVO, BookDTO.class));
            dtoList.add(dto);
        }
        // 전체 갯수
        int total = myBookListDAO.getCount(pageRequestDTO);

        PageResponseDTO<OrderInfoDTO> pageResponseDTO = PageResponseDTO.<OrderInfoDTO>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResponseDTO;
    }

}

