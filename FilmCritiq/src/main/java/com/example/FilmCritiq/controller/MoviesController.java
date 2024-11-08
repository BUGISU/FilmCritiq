package com.example.FilmCritiq.controller;

import com.example.FilmCritiq.dto.MoviesDTO;
import com.example.FilmCritiq.dto.PageRequestDTO;
import com.example.FilmCritiq.service.MoviesService;
import com.example.FilmCritiq.service.ReviewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

@Controller
@Log4j2
@RequestMapping({"/movies"})
@RequiredArgsConstructor
public class MoviesController {
  private final MoviesService moviesService;
  private final ReviewsService reviewsService;
  private static final Logger logger = LoggerFactory.getLogger(MoviesController.class);

  @GetMapping({"/register","/movieDetailRegister"})
  public void register() {
  }

  @PostMapping({"/register","/movieDetailRegister"})
  public String registerPost(MoviesDTO moviesDTO, RedirectAttributes ra) {
    Long mno = moviesService.register(moviesDTO);
    ra.addFlashAttribute("msg", mno);
    return "redirect:/movies/list";
  }

  @GetMapping({"","/","/list"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    model.addAttribute("pageResultDTO", moviesService.getList(pageRequestDTO));
    return "/movies/list";
  }

  @GetMapping({"/read", "/modify","/movieDetailView","/movieModify"})
  public void getMovies( Long mno, PageRequestDTO pageRequestDTO, Model model) {
    logger.info("getMovies called with mno: {}", mno);
    MoviesDTO moviesDTO = moviesService.getMovies(mno);
    typeKeywordInit(pageRequestDTO);
    model.addAttribute("moviesDTO", moviesDTO);
  }
  @PostMapping({"/modify","/movieModify"})
  public String modify(MoviesDTO dto, RedirectAttributes ra, PageRequestDTO pageRequestDTO){
    log.info("modify post... dto: " + dto);
    moviesService.modify(dto);
    typeKeywordInit(pageRequestDTO);
    ra.addFlashAttribute("msg", dto.getMno() + " 수정");
    ra.addAttribute("mno", dto.getMno());
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/movies/movieDetailView";
  }

  @Value("${com.example.upload.path}")
  private String uploadPath;

  @PostMapping("/remove")
  public String remove(Long mno, RedirectAttributes ra, PageRequestDTO pageRequestDTO){
    log.info("remove post... mno: " + mno);
    List<String> result = moviesService.removeWithReviewsAndPhotos(mno);
    log.info("result>>"+result);
    result.forEach(fileName -> {
      try {
        log.info("removeFile............"+fileName);
        String srcFileName = URLDecoder.decode(fileName, "UTF-8");
        File file = new File(uploadPath + File.separator + srcFileName);
        file.delete();
        File thumb = new File(file.getParent(),"s_"+file.getName());
        thumb.delete();
      } catch (Exception e) {
        log.info("remove file : "+e.getMessage());
      }
    });
    if(moviesService.getList(pageRequestDTO).getDtoList().size() == 0 && pageRequestDTO.getPage() != 1) {
      pageRequestDTO.setPage(pageRequestDTO.getPage()-1);
    }
    typeKeywordInit(pageRequestDTO);
    ra.addFlashAttribute("msg", mno + " 삭제");
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/movies/list";
  }
  private void typeKeywordInit(PageRequestDTO pageRequestDTO){
    if (pageRequestDTO.getType().equals("null")) pageRequestDTO.setType("");
    if (pageRequestDTO.getKeyword().equals("null")) pageRequestDTO.setKeyword("");
  }
}
