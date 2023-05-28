package com.project.silbaram.controller;

import com.project.silbaram.dto.BoardDTO;
import com.project.silbaram.dto.PageRequestDTO;
import com.project.silbaram.dto.PageResponseDTO;
import com.project.silbaram.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminBoardController {
    private final BoardService boardService;

    @GetMapping("/admin_board_list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("문의글 " + pageRequestDTO);

        //log.info("now {}", boardService.getNow());
        PageResponseDTO responseDTO = boardService.listAll(pageRequestDTO);
        log.info("responseDTO {}", responseDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
    }

    @GetMapping("/admin_board_info")
    public void read(Long bdid, PageRequestDTO pageRequestDTO, Model model) {
        BoardDTO boardDTO = boardService.getOneBoardById(bdid);
        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);

    }

    @GetMapping("/modify_qna")
    public void modify(Long bdid, PageRequestDTO pageRequestDTO, Model model) {
        BoardDTO boardDTO = boardService.getOneBoardById(bdid);
        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
    }

    @PostMapping("/modify_qna")
    public String modify(PageRequestDTO pageRequestDTO, @Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) { //유효성 검가 결과 에러가 있으면 수정페이지로 돌아감
        log.info("----------modify---------");
        if (bindingResult.hasErrors()) {
            log.info("modify has error...");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bdid", boardDTO.getBdid()); //tno가 쿼리스트링(like '?xx=1')으로 전달
            return "redirect:/admin/modify_qna";
        }
        log.info(boardDTO);
        boardService.modify(boardDTO);

        redirectAttributes.addAttribute("bdid", boardDTO.getBdid());

        return "redirect:/admin/admin_board_info";
    }

    @PostMapping("/qna_remove")
    public String remove(Long bdid, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
        log.info("-------------remove------------");
        log.info("bdid: " + bdid);
        log.info("bdid: " + pageRequestDTO);

        boardService.adminBoardRemove(bdid);

        return "redirect:/admin/admin_board_list?" + pageRequestDTO.getLink();
    }
}
