package hive.album.controller;

import hive.album.entity.Something;
import hive.album.storage.iStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class AlbumController {
  @GetMapping("/")
  public String a(){
    return "a";
  }
  @GetMapping("/sum")
  public Something sumNumbers(@RequestParam(value = "n1", required = false) String n1,
                              @RequestParam(value = "n2", required = false) String n2) {
    var algo  =new Something();
    int num1=Integer.parseInt(n1);
    num1+= Integer.parseInt(n2);
    algo.setContent("soma "+(num1));
    return algo;
  }
  private final iStorageService storageService;

  @Autowired
  public AlbumController(iStorageService storageService) {
    this.storageService = storageService;
  }
/*
  @GetMapping("/")
  public String listUploadedImages(Model model) throws IOException {
    model.addAttribute("files", storageService.loadAll().map(
        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
            "serveFile", path.getFileName().toString()).build().toString())
        .collect(Collectors.toList()));
    return "uploadForm";
  }
*/
  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

    Resource file = storageService.loadAsResource(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @PostMapping("/")
  public String handleFileUpload(@RequestParam("file") MultipartFile file) {
    storageService.store(file);
    return "{Mensagem:Sucesso}";
  }

}
