package com.six.sense.presentation.screen.materialComponents.graph

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries

@Composable
fun ChartScreen(modifier: Modifier = Modifier) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val lineModelProducer = remember { CartesianChartModelProducer() }
    LazyColumn {

        //BarChart
        item {
            Text("Bar Chart")
            Spacer(modifier = Modifier.height(10.dp))
            LaunchedEffect(Unit) {
                modelProducer.runTransaction {
                    columnSeries { series(5, 6, 5, 2, 11, 8, 5, 2, 15, 11, 8, 13, 12, 10, 2, 7) }
                }
            }
            CartesianChartHost(
                rememberCartesianChart(
                    rememberColumnCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom()
                ),
                modelProducer
            )
        }
        //PieChart
        item {
            Text("Line Chart")
            Spacer(modifier = Modifier.height(10.dp))
            LaunchedEffect(Unit) {
                lineModelProducer.runTransaction {
                    lineSeries {
                        series(5, 6, 5, 2, 11, 8, 5, 2, 15, 11, 8, 13, 12, 10, 2, 7)
                    }
                }
            }
            CartesianChartHost(
                rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom()
                ),
                lineModelProducer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BarChartPreview() {
    ChartScreen()
}