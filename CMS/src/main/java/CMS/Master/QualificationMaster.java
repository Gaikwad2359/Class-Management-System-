package CMS.Master;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tblqualification")

public class QualificationMaster {
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer qualId;
	private String qualName;
	private String qualDesc;
	public Integer getQualId() {
		return qualId;
	}
	public void setQualId(Integer qualId) {
		this.qualId = qualId;
	}
	public String getQualName() {
		return qualName;
	}
	public void setQualName(String qualName) {
		this.qualName = qualName;
	}
	public String getQualDesc() {
		return qualDesc;
	}
	public void setQualDesc(String qualDesc) {
		this.qualDesc = qualDesc;
	}
	public QualificationMaster() {
		super();
	}
	public QualificationMaster(Integer qualId, String qualName, String qualDesc) {
		super();
		this.qualId = qualId;
		this.qualName = qualName;
		this.qualDesc = qualDesc;
	}
	@Override
	public String toString() {
		return "QualificationMaster [qualId=" + qualId + ", qualName=" + qualName + ", qualDesc=" + qualDesc + "]";
	}
}