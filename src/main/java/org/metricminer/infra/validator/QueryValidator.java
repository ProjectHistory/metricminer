package org.metricminer.infra.validator;

import org.metricminer.controller.QueryController;
import org.metricminer.model.Author;
import org.metricminer.model.Query;
import org.metricminer.model.User;

import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Component
public class QueryValidator {

    static final String SOURCECODE_MESSAGE = "It is forbidden to get source code from projects";
    static final String DIFF_MESSAGE = "It is forbidden to get diff from projects";
    static final String WILDCARD_MESSAGE = "The query should not contain '*' wildcard";
    static String SECRETNAME_MESSAGE = "It is forbidden to get author's names, try using 'AuthorName()' function instead "
            + Author.NAME_COLUMN;
    static String SECRETEMAIL_MESSAGE = "It is forbidden to get author's emails, try using 'AuthorEmail()' function instead "
            + Author.EMAIL_COLUMN;
    static String NOT_ALLOWED_MESSAGE = "You are not allowed to update this query";
    private final Validator validator;

    public QueryValidator(Validator validator) {
        this.validator = validator;
    }

    private boolean containsWildCard(Query query) {
        boolean invalid = false;
        String sql = query.getSqlQuery();
        if (sql.contains("*")) {
            invalid = true;
        }
        return invalid;
    }

    public void validate(Query query) {
    	if (query.getName() == null || query.getName().isEmpty()) {
    		validator.add(new ValidationMessage("The name of the query cannot be empty",
    				"Validation error"));
    	}
    	if (query.getSqlQuery() == null || query.getSqlQuery().isEmpty()) {
    		validator.add(new ValidationMessage("The query cannot be empty",
    				"Validation error"));
    		return;
    	}
        if (containsWildCard(query)) {
            validator.add(new ValidationMessage(WILDCARD_MESSAGE,
                    "InvalidQuery"));
        }
        if (containsDiff(query)) {
        	validator.add(new ValidationMessage(DIFF_MESSAGE,
        			"InvalidQuery"));
        }
    }

    private boolean containsDiff(Query query) {
    	boolean invalid = false;
    	String sql = query.getSqlQuery();
    	if (sql.contains("diff")) {
    		invalid = true;
    	}
    	return invalid;
    }
    

    public void validateEditByAuthor(Query query, User user) {
        if (!query.isAllowedToEdit(user)) {
            validator.add(new ValidationMessage(NOT_ALLOWED_MESSAGE,
                    "InvalidQuery"));
        }
        validator.onErrorRedirectTo(QueryController.class).editQueryForm(query.getId());
    }


}
