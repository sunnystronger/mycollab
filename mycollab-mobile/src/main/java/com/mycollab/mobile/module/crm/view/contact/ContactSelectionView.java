/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.mycollab.mobile.ui.AbstractSelectionView;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ContactSelectionView extends AbstractSelectionView<SimpleContact> {
    private static final long serialVersionUID = 7742786524816492321L;
    private ContactListDisplay itemList;

    private ContactRowDisplayHandler rowHandler = new ContactRowDisplayHandler();

    public ContactSelectionView() {
        super();
        createUI();
        this.setCaption(AppContext.getMessage(ContactI18nEnum.M_VIEW_CONTACT_NAME_LOOKUP));
    }

    public void createUI() {
        itemList = new ContactListDisplay();
        itemList.setWidth("100%");
        itemList.setRowDisplayHandler(rowHandler);
        this.setContent(itemList);
    }

    @Override
    public void load() {
        ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        itemList.search(searchCriteria);

        SimpleContact clearContact = new SimpleContact();
        itemList.getListContainer().addComponentAsFirst(rowHandler.generateRow(clearContact, 0));
    }

    private class ContactRowDisplayHandler implements RowDisplayHandler<SimpleContact> {

        @Override
        public Component generateRow(final SimpleContact contact, int rowIndex) {
            Button b = new Button(contact.getContactName(), new Button.ClickListener() {
                private static final long serialVersionUID = -5218323555163873836L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    selectionField.fireValueChange(contact);
                    ContactSelectionView.this.getNavigationManager().navigateBack();
                }
            });
            return b;
        }

    }
}
