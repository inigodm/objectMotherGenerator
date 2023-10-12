package inigo.objectMotherCreator.infraestructure.config

import java.util.*
import javax.swing.table.DefaultTableModel

class TableModelSpecial(data: Vector<Vector<String>>, columnNames: Vector<String>) : DefaultTableModel(data, columnNames) {

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return true
    }

    override fun setValueAt(aValue: Any, rowIndex: Int, columnIndex: Int) {
        (dataVector as Vector<Vector<String>>)[rowIndex][columnIndex] = "$aValue"
        fireTableCellUpdated(rowIndex, columnIndex)
    }

    fun cleanVoids() {
        var i = 0
        val toRemove = mutableListOf<Int>()
        for (vector in (dataVector as Vector<Vector<String>>)) {
            if (!vector.any { !it.isNullOrEmpty() }) {
                toRemove.addFirst(i)
            }
            i++
        }
        if (!toRemove.isEmpty()) {
            toRemove.forEach { dataVector.removeAt(it) }
            fireTableDataChanged()
        }
    }
}

fun <E> MutableList<E>.addFirst(e: E) {
    this.add(0, e)
}
