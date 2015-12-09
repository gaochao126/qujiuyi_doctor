package com.jiuyi.doctor.fileupload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;

@Controller
@RequestMapping("/file")
public class FileUploadController extends ControllerBase {

	@Autowired
	@Qualifier("FileUploadService")
	FileUploadService service;

	public FileUploadController() throws Exception {
		super("/file");
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String uplaod() {
		return "fileupload";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public ServerResult handleFileUpload(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			try {
				byte[] bytes = file.getBytes();
				String path = "D:/files/" + fileName;
				File diskFile = new File(path);
				if (!diskFile.exists()) {
					diskFile.createNewFile();
				}
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(diskFile));
				stream.write(bytes);
				stream.close();
				return new ServerResult();
			} catch (Exception e) {
				return new ServerResult(ResultConst.FAIL);
			}
		} else {
			return new ServerResult(ResultConst.FAIL);
		}
	}
}
