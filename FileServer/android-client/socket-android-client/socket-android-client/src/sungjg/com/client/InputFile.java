package sungjg.com.client;

public class InputFile {
	private String path;
	private String name;
	private String ext;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public InputFile(String path, String name, String ext) {
		this.path = path;
		this.name = name;
		this.ext = ext;
	}
	public InputFile() {
	}
	
	
}
