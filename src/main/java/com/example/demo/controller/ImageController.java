package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Repo.ImageRepo;
import com.example.demo.model.ImageModel;
import com.example.demo.model.YouFramResponse;
import com.example.demo.service.ImageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

@RestController
@CrossOrigin(origins = "*")
public class ImageController {

	@Autowired
	ImageService imageService;

	@RequestMapping(value = "/saveImage", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public YouFramResponse upLoadImage(@RequestParam("imageFile") MultipartFile file) {
		return imageService.upLoadImage(file);
	}

	@RequestMapping(value = "/getAllImages", method = RequestMethod.GET)
	public List<ImageModel> getImages() {
		return imageService.getImages();
	}

	@RequestMapping(value = "/images/{filename:.+}", method = RequestMethod.GET)
	public ResponseEntity<org.springframework.core.io.Resource> downloadEnterpriseProfilePhoto(@PathVariable String filename){
		return imageService.getImage(filename);
	}

	
	
	
}
