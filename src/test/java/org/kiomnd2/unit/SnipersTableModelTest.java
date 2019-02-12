package org.kiomnd2.unit;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.kiomnd2.java.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.kiomnd2.java.SniperSnapshot.SniperState.*;
import static org.kiomnd2.java.SniperSnapshot.*;

public class SnipersTableModelTest {
    private final Mockery context = new Mockery();
    private TableModelListener listener = context.mock(TableModelListener.class);
    private final MainWindow.SnipersTableModel model = new MainWindow.SnipersTableModel();


    @Before
    public void attachModelListener() {
        model.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat(model.getColumnCount(), CoreMatchers.equalTo(Column.values().length));
    }

    @Test
    public void setsSniperValueInColumns() {
        context.checking(new Expectations(){{
            oneOf(listener).tableChanged(with(aRowChangedEvent()));
        }});
        model.sniperStatusChanged(new SniperSnapshot("item-56789",555,666, BIDDING));

        assertColumnEquals(Column.ITEM_IDENTIFIER, "item-56789");
        assertColumnEquals(Column.LAST_PRICE,555);
        assertColumnEquals(Column.LAST_BID, 666);
        assertColumnEquals(Column.SNIPER_STATUS ,Main.STATUS_BIDDING);

    }

    private void assertColumnEquals(Column column, Object expected) {
        final int rowIndex =0 ;
        final int columnIndex = column.ordinal();
        assertEquals(expected ,model.getValueAt(rowIndex,columnIndex));

    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(model,0));
    }





}
