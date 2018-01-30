package org.entando.entando.web.group.validator;

import org.entando.entando.web.group.GroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.agiletec.aps.system.services.group.IGroupManager;

/**
 * Validazioni applicative o complesse (non realiz<zabili tramite annotations
 */

@Component
public class GroupValidator implements Validator {

	@Autowired
	private IGroupManager groupManager;

	@Override
	public boolean supports(Class<?> paramClass) {

		return GroupRequest.class.equals(paramClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		GroupRequest request = (GroupRequest) target;
		String groupName = request.getName();
		if (null != groupManager.getGroup(groupName)) {
			//errors.reject -->  equivale ad un global error
			//errors.errors.rejectValue -->  equivale ad un field error
			errors.reject("1", new String[] { groupName }, "group.exists");
		}
	}

}
