package my.websecurity.taglibs.out;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import my.websecurity.taglibs.SecurityTagSupport;


/**
 * webSecurity系统标签out的支持类
 * 
 * @author xiegang
 * @since 2011-12-28
 */
public class SecurityOutTagSupport extends SecurityTagSupport {
	private static final long serialVersionUID = 365359909660274931L;
	public static char[][] specialCharactersRepresentation = new char[63][];
	static {
	  specialCharactersRepresentation[38] = "&amp;".toCharArray();
	  specialCharactersRepresentation[60] = "&lt;".toCharArray();
	  specialCharactersRepresentation[62] = "&gt;".toCharArray();
	  specialCharactersRepresentation[34] = "&#034;".toCharArray();
	  specialCharactersRepresentation[39] = "&#039;".toCharArray();
	}
	
	protected Object value;
	protected String def;
	protected boolean escapeXml;
	 
	 public SecurityOutTagSupport() {
		super();
		init();
	}

	private void init() {
	   this.value = (this.def = null);
	   this.escapeXml = true;
	 }
	 
	 public void release() {
	   super.release();
	   init();
	 }
	 
	public static void out(PageContext pageContext, boolean escapeXml, Object obj) throws IOException {
		JspWriter w = pageContext.getOut();
		if (!(escapeXml)) {
			if (obj instanceof Reader) {
				Reader reader = (Reader) obj;
				char[] buf = new char[4096];
				int count;
				while ((count = reader.read(buf, 0, 4096)) != -1) {
					w.write(buf, 0, count);
				}
			} else {
				w.write(obj.toString());
			}

		} else if (obj instanceof Reader) {
			Reader reader = (Reader) obj;
			char[] buf = new char[4096];
			int count;
			while ((count = reader.read(buf, 0, 4096)) != -1) {
				writeEscapedXml(buf, count, w);
			}
		} else {
			String text = obj.toString();
			writeEscapedXml(text.toCharArray(), text.length(), w);
		}
	}
	
	private static void writeEscapedXml(char[] buffer, int length, JspWriter w) throws IOException {
		int start = 0;
		for (int i = 0; i < length; ++i) {
			char c = buffer[i];
			if (c <= '>') {
				char[] escaped = specialCharactersRepresentation[c];
				if (escaped == null) continue;
				if (start < i) {
					w.write(buffer, start, i - start);
				}
				w.write(escaped);
				start = i + 1;
			}

		}
		if (start < length) w.write(buffer, start, length - start);
	}
}
