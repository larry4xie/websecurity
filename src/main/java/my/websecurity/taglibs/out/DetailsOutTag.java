package my.websecurity.taglibs.out;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import my.websecurity.support.SecurityDomainConfiguration;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;


/**
 * ${sessionScope[sessionScope["SEC-DOMAIN-USER"]].name} <br/>
 * property el表达式格式<br/>
 * &lt;sec:details domain = "user" property = "name" default =  escapeXml="true" &gt;
 * 
 * @author xiegang
 * @since 2011-12-30
 *
 */
public class DetailsOutTag extends SecurityOutTagSupport {
	private static final long serialVersionUID = -3643516268099773481L;
	private String property;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		try {
			SecurityDomainConfiguration config = getSecurityDomainConfiguration();
			if(config != null) {
				String[] names = config.getSession();
				if(names != null) {
					for(int i = 0; i < names.length; i++) {
						this.value = ExpressionEvaluatorManager.evaluate("property", "${sessionScope." + names[i] + "." + property + "}", Object.class, this, this.pageContext);
						if(this.value != null) break;
					}
				}
			}
			if (this.value != null) {
				out(this.pageContext, this.escapeXml, this.value);
				return SKIP_BODY;
			}
			if (this.def != null) {
				out(this.pageContext, this.escapeXml, this.def);
			}
			return SKIP_BODY;
		} catch (IOException ex) {
			throw new JspException(ex.toString(), ex);
		}
	 }
	
	/**
	 * 属性,支持链式表达式
	 * @param property
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	
	public void setDefault(String default_) {
		this.def = isEmpty(default_) ? "" : default_;
	}
	
	public void setEscapeXml(String escapeXml) {
		this.escapeXml = !"false".equals(escapeXml);
	}
}
