package dev.bandb.graphview.layouts.tree

import android.graphics.*
import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.AbstractGraphAdapter


open class TreeEdgeDecoration constructor(
        private val linePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 5f
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            pathEffect = CornerPathEffect(10f) },
        private val specialLinePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 3f
            color = Color.parseColor("#EF9A9A")
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            pathEffect = ComposePathEffect(
                    CornerPathEffect(10f),
                    DashPathEffect(floatArrayOf(50f, 20f), 0f)
            )
        }
) : RecyclerView.ItemDecoration() {

    private val linePath = Path()
    private val specialLinePath = Path()
    private val specialArrowPath = Path()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter
        if (parent.layoutManager == null || adapter == null) {
            return
        }
        if (adapter !is AbstractGraphAdapter) {
            throw RuntimeException(
                    "TreeEdgeDecoration only works with ${AbstractGraphAdapter::class.simpleName}")
        }
        val layout = parent.layoutManager
        if (layout !is BuchheimWalkerLayoutManager) {
            throw RuntimeException(
                    "TreeEdgeDecoration only works with ${BuchheimWalkerLayoutManager::class.simpleName}")
        }

        val configuration = layout.configuration

        val graph = adapter.graph
        if (graph != null && graph.hasNodes()) {
            val nodes = graph.nodes

            for (node in nodes) {
                val children = graph.successorsOf(node)
                val specialChildren = graph.specialSuccessorsOf(node)

                for (child in children) {
                    linePath.reset()
                    when (configuration.orientation) {
                        BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM -> {
                            // position at the middle-top of the child
                            linePath.moveTo(child.x + child.width / 2f, child.y)
                            // draws a line from the child's middle-top halfway up to its parent
                            linePath.lineTo(
                                    child.x + child.width / 2f,
                                    child.y - configuration.levelSeparation / 2f
                            )
                            // draws a line from the previous point to the middle of the parents width
                            linePath.lineTo(
                                    node.x + node.width / 2f,
                                    child.y - configuration.levelSeparation / 2f
                            )

                            // position at the middle of the level separation under the parent
                            linePath.moveTo(
                                    node.x + node.width / 2f,
                                    child.y - configuration.levelSeparation / 2f
                            )
                            // draws a line up to the parents middle-bottom
                            linePath.lineTo(
                                    node.x + node.width / 2f,
                                    node.y + node.height
                            )
                        }
                        BuchheimWalkerConfiguration.ORIENTATION_BOTTOM_TOP -> {
                            linePath.moveTo(child.x + child.width / 2f, child.y + child.height)
                            linePath.lineTo(
                                    child.x + child.width / 2f,
                                    child.y + child.height.toFloat() + configuration.levelSeparation / 2f
                            )
                            linePath.lineTo(
                                    node.x + node.width / 2f,
                                    child.y + child.height.toFloat() + configuration.levelSeparation / 2f
                            )

                            linePath.moveTo(
                                    node.x + node.width / 2f,
                                    child.y + child.height.toFloat() + configuration.levelSeparation / 2f
                            )
                            linePath.lineTo(
                                    node.x + node.width / 2f,
                                    node.y + node.height
                            )
                        }
                        BuchheimWalkerConfiguration.ORIENTATION_LEFT_RIGHT -> {
                            linePath.moveTo(child.x, child.y + child.height / 2f)
                            linePath.lineTo(
                                    child.x - configuration.levelSeparation / 2f,
                                    child.y + child.height / 2f
                            )
                            linePath.lineTo(
                                    child.x - configuration.levelSeparation / 2f,
                                    node.y + node.height / 2f
                            )

                            linePath.moveTo(
                                    child.x - configuration.levelSeparation / 2f,
                                    node.y + node.height / 2f
                            )
                            linePath.lineTo(
                                    node.x + node.width,
                                    node.y + node.height / 2f
                            )
                        }
                        BuchheimWalkerConfiguration.ORIENTATION_RIGHT_LEFT -> {
                            linePath.moveTo(child.x + child.width, child.y + child.height / 2f)
                            linePath.lineTo(
                                    child.x + child.width.toFloat() + configuration.levelSeparation / 2f,
                                    child.y + child.height / 2f
                            )
                            linePath.lineTo(
                                    child.x + child.width.toFloat() + configuration.levelSeparation / 2f,
                                    node.y + node.height / 2f
                            )

                            linePath.moveTo(
                                    child.x + child.width.toFloat() + configuration.levelSeparation / 2f,
                                    node.y + node.height / 2f
                            )
                            linePath.lineTo(
                                    node.x + node.width,
                                    node.y + node.height / 2f
                            )
                        }
                    }

                    c.drawPath(linePath, linePaint)
                }

                for (specialChild in specialChildren) {
                    specialLinePath.reset()

                    /*specialLinePath.moveTo(specialChild.x + specialChild.width / 2f, specialChild.y + specialChild.height)
                    specialLinePath.lineTo(
                            specialChild.x + specialChild.width / 2f,
                            specialChild.y + specialChild.height.toFloat() + configuration.levelSeparation / 4f
                    )
                    specialLinePath.lineTo(
                            node.x + node.width / 2f,
                            specialChild.y + specialChild.height.toFloat() + configuration.levelSeparation / 4f
                    )

                    specialLinePath.moveTo(
                            node.x + node.width / 2f,
                            specialChild.y + specialChild.height.toFloat() + configuration.levelSeparation / 4f
                    )
                    specialLinePath.lineTo(
                            node.x + node.width / 2f,
                            node.y + node.height
                    )*/

                    specialArrowPath.reset()
                    /*specialArrowPath.moveTo(
                            specialChild.x + specialChild.width / 2f,
                            specialChild.y + specialChild.height.toFloat()
                    )
                    specialArrowPath.lineTo(
                            specialChild.x + specialChild.width / 2.2f,
                            specialChild.y + specialChild.height.toFloat() + configuration.levelSeparation / 6f
                    )
                    specialArrowPath.lineTo(
                            specialChild.x + specialChild.width / 1.8f,
                            specialChild.y + specialChild.height.toFloat() + configuration.levelSeparation / 6f
                    )
                    specialArrowPath.lineTo(
                            specialChild.x + specialChild.width / 2f,
                            specialChild.y + specialChild.height.toFloat()
                    )*/

                    when (configuration.orientation) {
                        BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM -> {
                            specialLinePath.moveTo(specialChild.x + specialChild.width / 2f, specialChild.y + specialChild.height)
                            specialLinePath.lineTo(
                                    specialChild.x + specialChild.width / 2f,
                                    specialChild.y + specialChild.height.toFloat() + configuration.levelSeparation / 3f
                            )
                            specialLinePath.lineTo(
                                    node.x + node.width / 2f,
                                    specialChild.y + specialChild.height.toFloat() + configuration.levelSeparation / 3f
                            )

                            specialLinePath.moveTo(
                                    node.x + node.width / 2f,
                                    specialChild.y + specialChild.height.toFloat() + configuration.levelSeparation / 3f
                            )
                            specialLinePath.lineTo(
                                    node.x + node.width / 2f,
                                    node.y + node.height
                            )

                            // Arrows
                            specialArrowPath.moveTo(
                                    specialChild.x + specialChild.width / 2f,
                                    specialChild.y + specialChild.height.toFloat()
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x + specialChild.width / 2.2f,
                                    specialChild.y + specialChild.height.toFloat() + configuration.levelSeparation / 6f
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x + specialChild.width / 1.8f,
                                    specialChild.y + specialChild.height.toFloat() + configuration.levelSeparation / 6f
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x + specialChild.width / 2f,
                                    specialChild.y + specialChild.height.toFloat()
                            )
                        }
                        BuchheimWalkerConfiguration.ORIENTATION_BOTTOM_TOP -> {
                            // position at the middle-top of the child
                            specialLinePath.moveTo(specialChild.x + specialChild.width / 2f, specialChild.y)
                            // draws a line from the specialChild's middle-top halfway up to its parent
                            specialLinePath.lineTo(
                                    specialChild.x + specialChild.width / 2f,
                                    specialChild.y - configuration.levelSeparation / 3f
                            )
                            // draws a line from the previous point to the middle of the parents width
                            specialLinePath.lineTo(
                                    node.x + node.width / 2f,
                                    specialChild.y - configuration.levelSeparation / 3f
                            )

                            // position at the middle of the level separation under the parent
                            specialLinePath.moveTo(
                                    node.x + node.width / 2f,
                                    specialChild.y - configuration.levelSeparation / 3f
                            )
                            // draws a line up to the parents middle-bottom
                            specialLinePath.lineTo(
                                    node.x + node.width / 2f,
                                    node.y + node.height
                            )

                            // Arrows
                            specialArrowPath.moveTo(
                                    specialChild.x + specialChild.width / 2f,
                                    specialChild.y
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x + specialChild.width / 2.2f,
                                    specialChild.y - configuration.levelSeparation / 6f
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x + specialChild.width / 1.8f,
                                    specialChild.y - configuration.levelSeparation / 6f
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x + specialChild.width / 2f,
                                    specialChild.y
                            )
                        }
                        BuchheimWalkerConfiguration.ORIENTATION_LEFT_RIGHT -> {
                            specialLinePath.moveTo(specialChild.x + specialChild.width, specialChild.y + specialChild.height / 2f)
                            specialLinePath.lineTo(
                                    specialChild.x + specialChild.width.toFloat() + configuration.levelSeparation / 3f,
                                    specialChild.y + specialChild.height / 2f
                            )
                            specialLinePath.lineTo(
                                    specialChild.x + specialChild.width.toFloat() + configuration.levelSeparation / 3f,
                                    node.y + node.height / 2f
                            )

                            specialLinePath.moveTo(
                                    specialChild.x + specialChild.width.toFloat() + configuration.levelSeparation / 3f,
                                    node.y + node.height / 2f
                            )
                            specialLinePath.lineTo(
                                    node.x + node.width,
                                    node.y + node.height / 2f
                            )

                            // Arrows
                            specialArrowPath.moveTo(
                                    specialChild.x + specialChild.width,
                                    specialChild.y + specialChild.height.toFloat() / 2f
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x + specialChild.width.toFloat() + configuration.levelSeparation / 6f,
                                    specialChild.y + specialChild.height / 2.5f
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x + specialChild.width.toFloat() + configuration.levelSeparation / 6f,
                                            specialChild.y + specialChild.height / 1.6f
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x + specialChild.width,
                                    specialChild.y + specialChild.height.toFloat() / 2f
                            )
                        }
                        BuchheimWalkerConfiguration.ORIENTATION_RIGHT_LEFT -> {
                            specialLinePath.moveTo(specialChild.x, specialChild.y + specialChild.height / 2f)
                            specialLinePath.lineTo(
                                    specialChild.x - configuration.levelSeparation / 3f,
                                    specialChild.y + specialChild.height / 2f
                            )
                            specialLinePath.lineTo(
                                    specialChild.x - configuration.levelSeparation / 3f,
                                    node.y + node.height / 2f
                            )

                            specialLinePath.moveTo(
                                    specialChild.x - configuration.levelSeparation / 3f,
                                    node.y + node.height / 2f
                            )
                            specialLinePath.lineTo(
                                    node.x + node.width,
                                    node.y + node.height / 2f
                            )

                            // Arrows
                            specialArrowPath.moveTo(
                                    specialChild.x,
                                    specialChild.y + specialChild.height.toFloat() / 2f
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x - configuration.levelSeparation / 6f,
                                    specialChild.y + specialChild.height / 2.5f
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x - configuration.levelSeparation / 6f,
                                    specialChild.y + specialChild.height / 1.6f
                            )
                            specialArrowPath.lineTo(
                                    specialChild.x,
                                    specialChild.y + specialChild.height.toFloat() / 2f
                            )
                        }
                    }

                    specialLinePaint.style = Paint.Style.STROKE
                    specialLinePaint.pathEffect = ComposePathEffect(
                            CornerPathEffect(10f),
                            DashPathEffect(floatArrayOf(50f, 20f), 0f)
                    )
                    c.drawPath(specialLinePath, specialLinePaint)

                    specialLinePaint.style = Paint.Style.FILL
                    specialLinePaint.pathEffect = CornerPathEffect(5f)
                    c.drawPath(specialArrowPath, specialLinePaint)
                }
            }
        }
        super.onDraw(c, parent, state)
    }
}