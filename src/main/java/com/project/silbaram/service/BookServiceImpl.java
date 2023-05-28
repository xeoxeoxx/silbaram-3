package com.project.silbaram.service;

import com.project.silbaram.dao.BookDAO;
import com.project.silbaram.dao.CategoryDAO;
import com.project.silbaram.dao.LanguageDAO;
import com.project.silbaram.dto.BookDTO;
import com.project.silbaram.dto.PageRequestDTO;
import com.project.silbaram.dto.PageResponseDTO;
import com.project.silbaram.vo.BookVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;
    private final ModelMapper modelMapper;

    private final LanguageDAO languageDAO;
    private final CategoryDAO categoryDAO;

    @Override
    public void insertBook(BookDTO bookDTO) {
        log.info(modelMapper);
        BookVO bookVO = modelMapper.map(bookDTO, BookVO.class);
        bookDAO.insertBook(bookVO);
        log.info(bookVO);
    }

    @Override
    public void modifyBook(BookDTO bookDTO){
        BookVO bookVO = modelMapper.map(bookDTO, BookVO.class);
        bookDAO.updateBook(modelMapper.map(bookDTO, BookVO.class));
        log.info(bookVO);
    }

    @Override
    public void updateBook(BookDTO bookDTO) {
        BookVO bookVO = modelMapper.map(bookDTO, BookVO.class);
        bookDAO.updateBook(bookVO);
        log.info(bookVO);
    }

    public BookDTO getBookById(Long id) {
        BookVO bookVO = bookDAO.getBookById(id);
        return modelMapper.map(bookVO, BookDTO.class);
    }

    @Override
    public List<BookDTO> getBookList() {
        List<BookVO> bookVOList = bookDAO.getBookList();
        return bookVOList.stream()
                .map(bookVO -> modelMapper.map(bookVO, BookDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBookById(Long bkid) {
        bookDAO.deleteBookById(bkid);
    }


    @Override
    public BookDTO readOne(Long bkid) {
        BookVO bookVO = bookDAO.selectOne(bkid);
        log.info("bookVO : " + bookVO);
        BookDTO bookDTO = modelMapper.map(bookVO, BookDTO.class);
        //vo로 받아서 DTO로 전달


        return bookDTO;
    }

    @Override
    public PageResponseDTO<BookDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("service - list");
        // !! 조건 검색 쿼리로 변경
        // List<BoardVO> voList = boardDAO.selectAll();
        List<BookVO> voList = bookDAO.selectList(pageRequestDTO);
        log.info(voList);
        List<BookDTO> dtoList = new ArrayList<>();
        for (BookVO bookVO : voList) {
            dtoList.add(modelMapper.map(bookVO, BookDTO.class));
        }
        int total = bookDAO.getCount(pageRequestDTO);

        PageResponseDTO<BookDTO> pageResponseDTO = PageResponseDTO.<BookDTO>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResponseDTO;

    }

    @Override
    public boolean isBuyerByBkIdAndMid(Map<String, Object> map) {
        return bookDAO.isBuyerByBkIdAndMid(map);
    }

    @Override
    public String getCategoryById(String cid) {
        return bookDAO.getCategoryById(cid);
    }

    @Override
    public int getCountByWriterKeyword(String keyword) {
        return bookDAO.getCountByWriterKeyword(keyword);
    }

    @Override
    public int getCountByBookKeyword(String keyword) {
        return bookDAO.getCountByBookKeyword(keyword);
    }

    @Override
    public List<Map<String, Object>> getBookBycategoryAndCnt(String keyword) {
        return bookDAO.getBookBycategoryAndCnt(keyword);
    }

    @Override
    public BookDTO readOneBook(Long bkid) {
        BookVO bookVO = bookDAO.getBookById(bkid);
        log.info(bookVO);
        BookDTO bookDTO = modelMapper.map(bookVO, BookDTO.class);
        log.info(bookDTO);

        return bookDTO;
    }

    @Override
    public PageResponseDTO<BookDTO> adminBookList(PageRequestDTO pageRequestDTO) {

        log.info("ReviewList,,,");
        List<BookDTO> voList = bookDAO.adminbookList(pageRequestDTO).stream()
                .map(dto -> modelMapper.map(dto, BookDTO.class)).collect(Collectors.toList());


        for (BookDTO dtoList : voList) {
            dtoList.setLanguageVO(languageDAO.getLanguageById(dtoList.getLid()));
            dtoList.setCategoryVO(categoryDAO.getById(dtoList.getCid()));
            dtoList.setLanguage(languageDAO.getLanguageById(dtoList.getLid()).getLanguage());
            dtoList.setCateName(categoryDAO.getById(dtoList.getCid()).getCateName());
        }
        log.info(voList);
        int total = bookDAO.getCount(pageRequestDTO);

        PageResponseDTO<BookDTO> pageResponseDTO = new PageResponseDTO<>(pageRequestDTO, voList, total);
        return pageResponseDTO;

    }

    @Override
    public BookDTO getOneBookById(Long bkid){
        BookVO bookVO = bookDAO.getOneBookById(bkid);
        log.info(bookVO);
        BookDTO bookDTO = modelMapper.map(bookVO, BookDTO.class);
        bookDTO.setCategoryVO(categoryDAO.getById(bookDTO.getCid()));
        bookDTO.setLanguageVO(languageDAO.getLanguageById(bookDTO.getLid()));
        log.info(bookDTO);

        return bookDTO;
    }

}