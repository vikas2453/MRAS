package com.diageo.mras.webservices.modals;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.diageo.mras.webservices.responses.ViewConsumerInGroupResponse;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewConsumerInGroupResponseList {
	@XmlElement
	private Collection<ViewConsumerInGroupResponse> viewConsumerinGroupList;

	public Collection<ViewConsumerInGroupResponse> getViewConsumerinGroupList() {
		return viewConsumerinGroupList;
	}

	public void setViewConsumerinGroupList(
			Collection<ViewConsumerInGroupResponse> viewConsumerinGroupList) {
		this.viewConsumerinGroupList = viewConsumerinGroupList;
	}
}
