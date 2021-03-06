package ua.khai.slynko.library.web.command.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.dao.CatalogItemDao;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.validation.model.BookForm;
import ua.khai.slynko.library.web.abstractCommand.Command;
import ua.khai.slynko.library.web.command.utils.CommandUtils;

/**
 * Login command.
 *
 * @author O.Slynko
 */
public class AddBookCommand extends Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        if (!isInputDataValid(request)) {
            return Constants.Path.PAGE_ADD_BOOK;
        } else {
            createCatalogItem(request);
            return Constants.Path.PAGE_HOME_REDERECT;
        }
    }

    private boolean isInputDataValid(HttpServletRequest request) {
        return buildAddBookForm(request)
                .validateAndPrefillRequestWithErrors(request);
    }

    private BookForm buildAddBookForm(HttpServletRequest request) {
        return new BookForm(
                request.getParameter("bookTitle"), request.getParameter("edition"),
                request.getParameter("author"), request.getParameter("publicationYear"),
                request.getParameter("instancesNumber"));
    }

    private void createCatalogItem(HttpServletRequest request) throws DBException {
        new CatalogItemDao().createCatalogItem(buildCatalogItem(request));
        populateRequestSuccess(request);
        CommandUtils.setRedirect(request);
    }

    private CatalogItem buildCatalogItem(HttpServletRequest request) {
        CatalogItem catalogItem = new CatalogItem();
        catalogItem.setTitle(request.getParameter("bookTitle"));
        catalogItem.setAuthor(request.getParameter("author"));
        catalogItem.setEdition(request.getParameter("edition"));
        catalogItem.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
        catalogItem.setInstancesNumber(Integer.parseInt(request.getParameter("instancesNumber")));
        return catalogItem;
    }

    private void populateRequestSuccess(HttpServletRequest request) {
        request.getSession().setAttribute("bookAddIsSuccessful", true);
    }
}