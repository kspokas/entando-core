package org.entando.entando.web.group.validator;

import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.web.group.GroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
			System.out.println("*********************************************");
			System.out.println(groupName);
			System.out.println("*********************************************");
			errors.reject("1", new String[] { groupName }, "group.exists");
		}
	}

}
