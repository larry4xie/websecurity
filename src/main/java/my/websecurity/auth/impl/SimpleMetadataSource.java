package my.websecurity.auth.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import my.websecurity.auth.AbstractStringMetadataSource;
import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.support.SecurityApplicaionContextHolder;
import my.websecurity.support.metadata.GrantedPrivilege;
import my.websecurity.support.metadata.impl.GrantedPrivilegeImpl;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


/**
 * 简单数据源<br/>
 * 使用xml文件配置数据源<br/>
 * 参见：resources/websecurity-metadatas.xml
 * 
 * @author xiegang
 * @since 2012-7-3
 *
 */
public class SimpleMetadataSource extends AbstractStringMetadataSource {
	private static final Logger logger = LoggerFactory.getLogger(SimpleMetadataSource.class);
	
	private String metadataFileLocation = "/WEB-INF/classes/websecurity-metadatas.xml";
	
	private static final String METADATAS_XSD = "resources/websecurity-metadatas-1.0.xsd";
	
	/**
	 * 读写锁
	 */
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	/**
	 * 所有的数据源集合
	 */
	private Map<String, Metadata> metadatas = new HashMap<String, Metadata>();
	
	@Override
	public List<Metadata> getMetadatas(Object object) throws SecurityRuntimeException {
		List<Metadata> ms = new ArrayList<Metadata>();
		// read lock
		lock.readLock().lock();
		try {
			for(Entry<String, Metadata> m : metadatas.entrySet()) {
				if(urlMatcher.pathMatchesUrl(urlMatcher.compile(m.getKey()), object.toString())) {
					ms.add(m.getValue());
				}
			}
		} finally {
			lock.readLock().unlock();
		}
		return ms;
	}
	
	/**
	 * 初始化Metadata
	 * 
	 * @throws SecurityRuntimeException
	 */
	public void initMetadata() throws SecurityRuntimeException {
		try {
			logger.info("init simple metadata config form: " + metadataFileLocation + "...");
			InputStream config = SecurityApplicaionContextHolder.getServletContext().getResourceAsStream(metadataFileLocation);
			SAXReader reader = new SAXReader(true);
			logger.debug("验证 websecurity-metadatas Schema ...");
			EntityResolver resolver = new EntityResolver() {
				public InputSource resolveEntity(String publicId, String systemId) {
					InputStream in = getClass().getClassLoader().getResourceAsStream(METADATAS_XSD);
					return new InputSource(in);
				}
			};
			reader.setEntityResolver(resolver);
			reader.setFeature("http://apache.org/xml/features/validation/schema", true);
			Document doc = reader.read(config);
			logger.debug("验证 websecurity-metadatas Schema 成功");
			
			List<?> resources = doc.selectNodes("//metadatas/resource");
			Map<String, Metadata> metadatas = new HashMap<String, Metadata>();
			for (Object o : resources) {
				if (o instanceof Element) {
					Element ele = (Element) o;
					String urlpattern = ele.valueOf("@urlpattern").trim();
					String access = ele.valueOf("@access").trim();
					String relation = ele.valueOf("@relation").trim();
					if(urlpattern.length() > 0 && access.length() > 0) {
						String[] arr = access.split(",");
						Collection<GrantedPrivilege> privileges = new HashSet<GrantedPrivilege>();
						for(String p : arr) {
							p = p.trim();
							if(p.length() > 0) {
								privileges.add(new GrantedPrivilegeImpl(p));
							}
						}
						Metadata metadata = new Metadata(urlpattern, privileges, relation);
						metadatas.put(urlpattern, metadata);
						if(logger.isDebugEnabled()) {
							logger.debug("resource [ " + metadata.getObject() + ", [ " + metadata.getPrivileges() + " ], " + metadata.getRelation() + "]");
						}
					}
				}
			}
			
			// write lock
			lock.writeLock().lock();
			try {
				this.metadatas = metadatas;
			} finally {
				lock.writeLock().unlock();
			}
			logger.info("init simple metadata config form: " + metadataFileLocation + " success!");
		} catch (Exception e) {
			logger.error("init simple metadata config form: " + metadataFileLocation + " error!");
			throw new SecurityRuntimeException("init simple metadata config form: " + metadataFileLocation + " error!", e);
		}
	}
	
	/**
	 * 重新加载Metadata
	 * 
	 * @throws SecurityRuntimeException
	 */
	public void reloadMetadata() throws SecurityRuntimeException {
		this.initMetadata();
	}
	
	/**
	 * @return the metadataFileLocation
	 */
	public String getMetadataFileLocation() {
		return metadataFileLocation;
	}

	/**
	 * @param metadataFileLocation the metadataFileLocation to set
	 */
	public void setMetadataFileLocation(String metadataFileLocation) {
		this.metadataFileLocation = metadataFileLocation;
		initMetadata();
	}
}
