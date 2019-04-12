package hive.album.controller;

import hive.album.storage.iStorageService;
import hive.common.security.HiveHeaders;
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
  @GetMapping("/smugshot/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile
  (@PathVariable String filename,
   @RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username)
  {
    Resource file = storageService.loadAsResource(filename);
    return ResponseEntity.ok().header(
            HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @PostMapping("/smugshot/")
  public String handleFileUpload(@RequestParam("file") MultipartFile file) {
    storageService.store(file);
    return "{Mensagem:Sucesso}";
  }
}
