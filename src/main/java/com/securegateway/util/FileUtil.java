package com.securegateway.util;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

/**
 * FileUtil – helpers for multipart file upload and safe file download.
 */
public final class FileUtil {

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    /** Maximum allowed upload size in bytes (10 MB). */
    public static final long MAX_FILE_SIZE = 10L * 1024 * 1024;

    /** Allowed MIME types for attachments. */
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "image/gif",
            "text/plain",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/zip"
    );

    private FileUtil() { /* utility class */ }

    /**
     * Saves an uploaded {@link FileItem} to {@code uploadDir} with a unique filename.
     *
     * @param fileItem  the uploaded file
     * @param uploadDir absolute path to the upload directory on disk
     * @return the unique filename under which the file was saved
     * @throws IOException              on I/O failure
     * @throws IllegalArgumentException for invalid or oversized files
     */
    public static String saveUploadedFile(FileItem fileItem, String uploadDir)
            throws IOException, IllegalArgumentException {

        if (fileItem == null || fileItem.getSize() == 0) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }
        if (fileItem.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the 10 MB limit.");
        }
        String contentType = fileItem.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("File type not allowed: " + contentType);
        }

        // Build a unique filename: timestamp_uuid_originalname
        String originalName = sanitizeFileName(fileItem.getName());
        String timestamp    = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid         = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String uniqueName   = timestamp + "_" + uuid + "_" + originalName;

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = new File(dir, uniqueName);
        try {
            fileItem.write(dest);
        } catch (Exception e) {
            throw new IOException("Failed to write uploaded file: " + e.getMessage(), e);
        }
        log.info("File saved: {}", dest.getAbsolutePath());
        return uniqueName;
    }

    /**
     * Determines the MIME type of a file by its name (fallback to octet-stream).
     */
    public static String getMimeType(String fileName) {
        try {
            String mime = Files.probeContentType(Paths.get(fileName));
            return mime != null ? mime : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    /**
     * Strips directory traversal characters and normalises the filename.
     */
    public static String sanitizeFileName(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            return "attachment";
        }
        // Keep only the filename portion (handles Windows paths too)
        String name = Paths.get(rawName).getFileName().toString();
        // Remove any remaining path separators or null bytes
        name = name.replaceAll("[/\\\\:*?\"<>|\\x00]", "_");
        return name.isBlank() ? "attachment" : name;
    }

    /**
     * Returns the real upload directory path, rooted at the web-app's upload folder.
     *
     * @param servletRealPath the value of {@code ServletContext.getRealPath("/uploads")}
     */
    public static String resolveUploadDir(String servletRealPath) {
        File dir = new File(servletRealPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }
}
