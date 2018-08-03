package org.kumoricon.staff.client;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public class ViewModel {

    private final ObjectProperty<Node> mainView = new SimpleObjectProperty(this, "mainView", null);
    private final BooleanProperty preferencesMenuDisabled = new SimpleBooleanProperty(this, "preferencesMenuDisabled", true);
    private final BooleanProperty refreshMenuDisabled = new SimpleBooleanProperty(this, "refreshMenuDisabled", true);

    public ObjectProperty<Node> mainViewProperty() {
        return mainView;
    }

    public final Node getMainView() {
        return mainView.get();
    }

    public final void setMainView(Node mainView) {
        this.mainView.set(mainView);
    }

    public final void disablePreferencesMenu(boolean disabled) {
        preferencesMenuDisabled.set(disabled);
    }

    public final BooleanProperty preferencesMenuDisabledProperty() {
        return preferencesMenuDisabled;
    }


    public final void disableRefreshMenu(boolean disabled) {
        refreshMenuDisabled.set(disabled);
    }
    public final BooleanProperty refreshMenuDisabledProperty() {
        return refreshMenuDisabled;
    }
}