package com.example.demo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Repo.ImageRepo;
import com.example.demo.model.ImageModel;
import com.example.demo.model.YouFramResponse;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

@Service
public class ImageService {

	@Autowired
	ImageRepo imageRepo;

	private Path fileStorageLocation = Paths.get("E:\\STSProject\\BulidMosaic\\YouFrame\\Images").toAbsolutePath().normalize();

	@Value("${baseUrl}")
	private String baseUrl;

	public YouFramResponse upLoadImage(MultipartFile file) {

		if (validFileFormat(file.getOriginalFilename())) {
			String targetFileName = saveImages(file);
			ImageModel model = new ImageModel(file.getOriginalFilename(), targetFileName);
			imageRepo.save(model);
			return new YouFramResponse("Image Uploaded");
		}else {
			return new YouFramResponse("InvalidFileFormat");
		}
		
	}

	private boolean validFileFormat(String originalFilename) {
		return (originalFilename.endsWith(".png") | originalFilename.endsWith(".jpg")| originalFilename.endsWith(".svg"));
	}

	public List<ImageModel> getImages() {
		return imageRepo.findAllByOrderByIdDesc();
	}

	public ResponseEntity<Resource> getImage(String filename) {
       System.out.println("image hit");
		Resource resource = null;
		Path filePath = this.fileStorageLocation.resolve(filename).normalize();
		try {
			resource = new UrlResource(filePath.toUri());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		String contentType = null;
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

	}

	private String saveImages(MultipartFile file) {
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
		System.out.println(originalFileName);
		String fileExtension = "";
		try {
			fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			System.out.println(fileExtension);
		} catch (Exception e) {
			fileExtension = "";
		}
		Path targetLocation = this.fileStorageLocation.resolve(originalFileName);
		try {
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String imageUrl = baseUrl + originalFileName;
		return imageUrl;

	}

}
