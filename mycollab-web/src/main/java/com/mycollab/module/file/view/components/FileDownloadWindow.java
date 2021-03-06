/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.file.view.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.domain.Resource;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.resources.StreamDownloadResourceUtil;
import com.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.mycollab.vaadin.web.ui.UserLink;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class FileDownloadWindow extends Window {
    private static final long serialVersionUID = 1L;
    private final Content content;

    public FileDownloadWindow(final Content content) {
        super(content.getName());
        this.setWidth("500px");
        this.center();
        this.setResizable(false);
        this.setModal(true);

        this.content = content;
        this.constructBody();
    }

    private void constructBody() {
        final MVerticalLayout layout = new MVerticalLayout().withFullWidth();
        CssLayout iconWrapper = new CssLayout();
        final ELabel iconEmbed = ELabel.fontIcon(FileAssetsUtil.getFileIconResource(content.getName()));
        iconEmbed.addStyleName("icon-48px");
        iconWrapper.addComponent(iconEmbed);
        layout.with(iconWrapper).withAlign(iconWrapper, Alignment.MIDDLE_CENTER);

        final GridFormLayoutHelper inforLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 4);

        if (content.getDescription() != null) {
            final Label descLbl = new Label();
            if (!content.getDescription().equals("")) {
                descLbl.setData(content.getDescription());
            } else {
                descLbl.setValue("&nbsp;");
                descLbl.setContentMode(ContentMode.HTML);
            }
            inforLayout.addComponent(descLbl, "Description", 0, 0);
        }

        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        SimpleUser user = userService.findUserByUserNameInAccount(content.getCreatedUser(), AppContext.getAccountId());
        if (user == null) {
            inforLayout.addComponent(new UserLink(AppContext.getUsername(), AppContext.getUserAvatarId(),
                    AppContext.getUserDisplayName()), "Created by", 0, 1);
        } else {
            inforLayout.addComponent(new UserLink(user.getUsername(), user.getAvatarid(), user.getDisplayName()), "Created by", 0, 1);
        }


        final Label size = new Label(FileUtils.getVolumeDisplay(content.getSize()));
        inforLayout.addComponent(size, "Size", 0, 2);

        ELabel dateCreate = new ELabel().prettyDateTime(content.getCreated().getTime());
        inforLayout.addComponent(dateCreate, "Created date", 0, 3);

        layout.addComponent(inforLayout.getLayout());

        final MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false));

        final Button downloadBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DOWNLOAD));
        List<Resource> resources = new ArrayList<>();
        resources.add(content);

        StreamResource downloadResource = StreamDownloadResourceUtil.getStreamResourceSupportExtDrive(resources);

        FileDownloader fileDownloader = new FileDownloader(downloadResource);
        fileDownloader.extend(downloadBtn);
        downloadBtn.setIcon(FontAwesome.DOWNLOAD);
        downloadBtn.addStyleName(UIConstants.BUTTON_ACTION);

        final Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        cancelBtn.addStyleName(UIConstants.BUTTON_OPTION);
        buttonControls.with(cancelBtn, downloadBtn).alignAll(Alignment.TOP_RIGHT);
        layout.with(buttonControls).withAlign(buttonControls, Alignment.TOP_RIGHT);
        this.setContent(layout);
    }
}
