package org.kiomnd2.unit;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.kiomnd2.java.Column;
import org.kiomnd2.java.Item;
import org.kiomnd2.java.SniperSnapshot;
import org.kiomnd2.java.SnipersTableModel;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SnipersTableModelTest {
    private final Mockery context = new Mockery();
    private TableModelListener listener = context.mock(TableModelListener.class);
    private final SnipersTableModel model = new SnipersTableModel();


    @Before
    public void attachModelListener() {
        model.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat( model.getColumnCount(), CoreMatchers.equalTo(Column.values().length));
    }

    @Test
    public void setsSniperValueInColumns() {
        SniperSnapshot joining = SniperSnapshot.joining(new Item("item 1", 555));
        SniperSnapshot bidding = joining.bidding(555,666);
        context.checking(new Expectations(){{
            allowing(listener).tableChanged(with(anyInsertionEvent()));
            oneOf(listener).tableChanged(with(aChangeInRow(0)));
        }});

        model.addSniper(joining);

        model.sniperStateChanged(bidding);

        assertRowMatchesSnapshot(0,bidding);

    }

    @Test
    public void setUpColumnHeadings() {
        for(Column column : Column.values()) {
            assertEquals(column.name, model.getColumnName(column.ordinal()));
        }
    }

    @Test
    public void notifiesListenersWhenAddinASniper() {
        SniperSnapshot joining = SniperSnapshot.joining(new Item("item 1", 5678));
        context.checking(new Expectations(){{
            oneOf(listener).tableChanged(with(anInsertionAtRow(0)));
        }});
        assertEquals(0,model.getRowCount());

        model.addSniper(joining);

        assertEquals(1,model.getRowCount());
        assertRowMatchesSnapshot(0,joining);
    }


    @Test
    public void holdsSnipersInAdditionOrder() {
        context.checking(new Expectations(){{
            ignoring(listener);
        }});

        model.addSniper(SniperSnapshot.joining(new Item("item 0", 1234)));
        model.addSniper(SniperSnapshot.joining(new Item("item 1", 1234)));

        assertEquals("item 0", cellValue(0, Column.ITEM_IDENTIFIER));
        assertEquals("item 1", cellValue(1, Column.ITEM_IDENTIFIER));
    }


    private void assertColumnEquals(Column column, Object expected) {
        final int rowIndex =0 ;
        final int columnIndex = column.ordinal();
        assertEquals(expected ,model.getValueAt(rowIndex,columnIndex));
    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(model,0));
    }



    private Matcher<TableModelEvent> anyInsertionEvent() {
        return hasProperty("type" ,equalTo(TableModelEvent.INSERT));
    }

    private Matcher<TableModelEvent> anInsertionAtRow(final int row){
        return samePropertyValuesAs(new TableModelEvent(model,row,row,TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
        assertEquals( snapshot.item.getIdentifier(), cellValue(row, Column.ITEM_IDENTIFIER));
        assertEquals( snapshot.lastPrice, cellValue(row, Column.LAST_PRICE));
        assertEquals( snapshot.lastBid, cellValue(row, Column.LAST_BID));
        assertEquals( SnipersTableModel.textFor(snapshot.state), cellValue(row, Column.SNIPER_STATUS));


    }

    private Object cellValue(int row, Column column) {
        return model.getValueAt(row, column.ordinal());
    }

    public Matcher<TableModelEvent> aChangeInRow(int row) {
        return samePropertyValuesAs(new TableModelEvent(model,row));
    }

}
