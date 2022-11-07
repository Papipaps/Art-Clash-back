package com.example.demo.controller;

import com.example.demo.model.data.Profil;
import com.example.demo.repository.ProfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.Deflater;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

  @Autowired
  ProfilRepository profilRepository;
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = userDetails.getUsername();
    Profil profil = profilRepository.findByUsername(username).get();
    return "Bonjour "+profil.getFirstname() +" "+profil.getLastname() +" !";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }

  @GetMapping(path = "/image")
  public byte[] extractBytes (String ImageName) throws IOException {
    // open image
    File imgPath = new File("C:/Users/Jojo/Pictures/Dessin/PNG/wip/avi.png");
    BufferedImage bufferedImage = ImageIO.read(imgPath);

    // get DataBufferBytes from Raster
    WritableRaster raster = bufferedImage .getRaster();
    DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

    return compressByteArray( data.getData() );
  }
  public static byte[] compressByteArray(byte[] bytes){

    ByteArrayOutputStream baos = null;
    Deflater dfl = new Deflater();
    dfl.setLevel(Deflater.BEST_COMPRESSION);
    dfl.setInput(bytes);
    dfl.finish();
    baos = new ByteArrayOutputStream();
    byte[] tmp = new byte[4*1024];
    try{
      while(!dfl.finished()){
        int size = dfl.deflate(tmp);
        baos.write(tmp, 0, size);
      }
    } catch (Exception ex){

    } finally {
      try{
        if(baos != null) baos.close();
      } catch(Exception ex){}
    }

    return baos.toByteArray();
  }
}
