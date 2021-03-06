package ch.so.agi.simi.web.screens.extended.browse;

import ch.so.agi.simi.entity.extended.*;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("simiExtended_Dependency.browse")
@UiDescriptor("extended-browse.xml")
@LookupComponent("dependenciesTable")
@LoadDataBeforeShow
public class ExtendedBrowse extends StandardLookup<Dependency> {
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<Dependency> dependenciesTable;
    @Inject
    private Metadata metdata;

    @Subscribe("createBtn.createReport")
    public void onCreateBtnCreateReport(Action.ActionPerformedEvent event) {
        Report report = metdata.create(Report.class);
        showCreateEditorForDependency(report);
    }

    @Subscribe("createBtn.createComponent")
    public void onCreateBtnCreateComponent(Action.ActionPerformedEvent event) {
        Component comp = metdata.create(Component.class);
        showCreateEditorForDependency(comp);
    }

    @Subscribe("createBtn.createFeatureinfo")
    public void onCreateBtnCreateFeatureinfo(Action.ActionPerformedEvent event) {
        FeatureInfo fi = metdata.create(FeatureInfo.class);
        showCreateEditorForDependency(fi);
    }

    @Subscribe("createBtn.createCCC")
    public void onCreateBtnCreateCCCIntegration(Action.ActionPerformedEvent event) {
        CCCIntegration ci = metdata.create(CCCIntegration.class);
        showCreateEditorForDependency(ci);
    }

    private void showCreateEditorForDependency(Dependency dependency) {
        screenBuilders.editor(dependenciesTable)
                .editEntity(dependency)
                .build()
                .show();
    }
}