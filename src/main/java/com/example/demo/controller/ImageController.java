package com.example.demo.controller;

import com.example.demo.dto.ImageDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Image;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.module.ResolutionException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;

    //1st Endpoint:- Upload functionality for image;-->saving image in database service.
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload successful", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse( e.getMessage(),null));
        }
    }

    //Response Entity is class to deliver HttpResponse
    //Parts of HttpResponse can be Status code, Header, Body etc.
    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<?> downloadImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            //ByteArrayResource is a Spring class that wraps
            // binary data (byte[]) and presents it as a downloadable resource.
            // ByteArrayResource is a spring interface which wraps byte object and present it as a resource
            //resource can be downloaded when sent in response entity;
            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType())) // Check MIME type validity
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"").body(resource);
        } catch (ResourceNotFoundException e) {
            Map<String, String> response=new HashMap<>();
            response.put(e.getMessage(), "image not found"+imageId);
            return ResponseEntity.status(NOT_FOUND).body(response); // Return 404 response
        }catch (SQLException e) {
            throw new RuntimeException("Failed to read Blob data", e); // Specific error handling
        } catch (Exception e) {
            throw new RuntimeException("Failed to download image", e); // Generic error handling
        }
    }

    //When you sends or upload a file from client side it is received as a Multipart file to server,
    // When you upload a file from the client to the backend, the file is typically received as a MultipartFile object in the backend.
    //When a client upload a file through form data it is received as MultipartFile with addition to metadata like filename, filetype as HttpRequest.
    //For file uploads from the client, you work with MultipartFile.
    //For file downloads (or sending a file to the client), you work with Resource (like ByteArrayResource).
    @PutMapping("image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            imageService.updateImage(file, imageId);
            return ResponseEntity.ok(new ApiResponse("Update success!", null));
        } catch (ResourceNotFoundException e ) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch(RuntimeException e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try{
            imageService.deleteImageById(imageId);
            return ResponseEntity.ok(new ApiResponse("delete success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
