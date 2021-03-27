package sugjg.com.dto;

public class FileStat {
	private String name;
	private String path;
	private String ext;
	private long size;
	private long tmpfileSize;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getTmpfileSize() {
		return tmpfileSize;
	}

	public void setTmpfileSize(long tmpfileSize) {
		this.tmpfileSize = tmpfileSize;
	}

	public FileStat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileStat(String name, String path, String ext, long size, long tmpfileSize) {
		super();
		this.name = name;
		this.path = path;
		this.ext = ext;
		this.size = size;
		this.tmpfileSize = tmpfileSize;
	}

	@Override
	public String toString() {
		return "FileStat [name=" + name + ", path=" + path + ", ext=" + ext + ", size=" + size + ", tmpfileSize="
				+ tmpfileSize + "]";
	}

}
