package com.project.silbaram.controller;

import com.project.silbaram.dto.*;
import com.project.silbaram.service.BookService;
import com.project.silbaram.service.CategoryService;
import com.project.silbaram.service.LanguageService;
import com.project.silbaram.vo.BookVO;
import com.project.silbaram.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminBookController {
    @Autowired
    private final BookService bookService;
    @Autowired
    private final LanguageService languageService;
    @Autowired
    private final CategoryService categoryService;

    @GetMapping("/admin_book_list")
    public void bookList(PageRequestDTO pageRequestDTO, Model model){
        log.info("책 목록 " + pageRequestDTO);

        //log.info("now {}", boardService.getNow());
        PageResponseDTO responseDTO = bookService.adminBookList(pageRequestDTO);
        log.info("responseDTO {}", responseDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
    }

    @GetMapping("/add_book")
    public String addBookGET(Model model) {
        log.info("addBookGET...");
        model.addAttribute("bookDTO", new BookDTO());
        return "/admin/add_book";
    }

    @PostMapping("/add_book")
    public String addBookPOST(@Valid BookDTO bookDTO, BindingResult bindingResult,
                              @RequestParam("name") String name, @RequestParam("lid") String lid,
                              @RequestParam("cid") String cid, Model model) throws IOException {
        //...

        Long categoryId = Long.parseLong(cid);
//        Long languageId = Long.parseLong(lid);

        bookDTO.setCid(categoryId);
//        bookDTO.setLid(languageId);

        // BookVO 객체 생성 및 필드 설정
        BookVO book = BookVO.builder()
                .name(name)
//                .lid(languageId)
                .cid(categoryId)
                .build();

        bookService.insertBook(bookDTO);
        return "redirect:/admin/admin_book_list";
    }

    @GetMapping("/admin_book_info")
    public String readOneBook(Long bkid, PageRequestDTO pageRequestDTO, Model model) {
        BookDTO bookDTO = bookService.getOneBookById(bkid);
        PageResponseDTO responseDTO = bookService.adminBookList(pageRequestDTO);
        log.info(bookDTO);
        model.addAttribute("dto", bookDTO);
        model.addAttribute("responseDTO", responseDTO);

        return "admin/admin_book_info";
    }

    @GetMapping("/modify_book")
    public String modifyBook(Long bkid, PageRequestDTO pageRequestDTO, Model model) {
        BookDTO bookDTO = bookService.getOneBookById(bkid);
        log.info(bookDTO);
        model.addAttribute("dto", bookDTO);

        return "/admin/modify_book";
    }

    @PostMapping("/modify_book")
    public String modify(PageRequestDTO pageRequestDTO, @Valid LanguageDTO languageDTO, @Valid CategoryVO categoryDTO, @Valid BookDTO bookDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) { //유효성 검가 결과 에러가 있으면 수정페이지로 돌아감
        log.info("----------modify---------");
        if (bindingResult.hasErrors()) {
            log.info("modify has error...");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bkid", bookDTO.getBkid()); //tno가 쿼리스트링(like '?xx=1')으로 전달
            return "redirect:/admin/modify_book?";
        }
        log.info(bookDTO);
        bookService.updateBook(bookDTO);

        redirectAttributes.addAttribute("bkid", bookDTO.getBkid());

        return "redirect:/admin/admin_book_info?";
    }

    @PostMapping("/remove")
    public String remove(Long bkid, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
        log.info("-------------remove------------");
        log.info("bkid: " + bkid);
        log.info("bkid: " + pageRequestDTO);

        bookService.deleteBookById(bkid);

        return "redirect:/admin/admin_book_list?" + pageRequestDTO.getLink();
    }

}

