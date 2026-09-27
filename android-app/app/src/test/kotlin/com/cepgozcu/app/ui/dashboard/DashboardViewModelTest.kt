package com.cepgozcu.app.ui.dashboard

import app.cash.turbine.test
import com.cepgozcu.app.fakes.FakeAgentApi
import com.cepgozcu.app.fakes.FakeConnectionSource
import com.cepgozcu.app.net.AgentApiException
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.CpuMetrics
import com.cepgozcu.app.net.protocol.DiskMetrics
import com.cepgozcu.app.net.protocol.MemoryMetrics
import com.cepgozcu.app.net.protocol.SystemMetrics
import com.cepgozcu.app.ui.common.UiState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private fun sampleMetrics(cpu: Double = 12.0) = SystemMetrics(
        ts = 1L,
        cpu = CpuMetrics(totalPercent = cpu, perCoreProcess = emptyList(), coreCount = 4),
        memory = MemoryMetrics(totalBytes = 1000, usedBytes = 400, availableBytes = 600),
        disks = listOf(DiskMetrics("C:", "OS", 100, 40, 60, isLowSpace = false)),
        machineName = "TEST-PC",
        osVersion = "Windows 11",
        uptime = "1.00:00:00",
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `starts Loading then moves to Content once connected and fetch succeeds`() = runTest(testDispatcher) {
        val api = FakeAgentApi().apply { nextMetrics = sampleMetrics() }
        val connection = FakeConnectionSource(initialApi = api)
        val viewModel = DashboardViewModel(connection)

        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(UiState.Loading::class.java)

            connection.setState(ConnectionState.Connected)

            val content = awaitItem() as UiState.Content
            assertThat(content.data.cpu.totalPercent).isEqualTo(12.0)
            assertThat(content.data.machineName).isEqualTo("TEST-PC")
        }
    }

    @Test
    fun `maps a network failure to Offline`() = runTest(testDispatcher) {
        val api = FakeAgentApi().apply { failureToThrow = java.io.IOException("no route to host") }
        val connection = FakeConnectionSource(initialApi = api)
        val viewModel = DashboardViewModel(connection)

        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(UiState.Loading::class.java)
            connection.setState(ConnectionState.Connected)
            assertThat(awaitItem()).isInstanceOf(UiState.Offline::class.java)
        }
    }

    @Test
    fun `maps an AgentApiException to Error carrying the error code`() = runTest(testDispatcher) {
        val api = FakeAgentApi().apply { failureToThrow = AgentApiException("internal_error") }
        val connection = FakeConnectionSource(initialApi = api)
        val viewModel = DashboardViewModel(connection)

        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(UiState.Loading::class.java)
            connection.setState(ConnectionState.Connected)
            val error = awaitItem() as UiState.Error
            assertThat(error.message).isEqualTo("internal_error")
        }
    }

    @Test
    fun `disconnecting after content keeps showing the last content, not Offline`() = runTest(testDispatcher) {
        val api = FakeAgentApi().apply { nextMetrics = sampleMetrics() }
        val connection = FakeConnectionSource(initialApi = api)
        val viewModel = DashboardViewModel(connection)

        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(UiState.Loading::class.java)
            connection.setState(ConnectionState.Connected)
            assertThat(awaitItem()).isInstanceOf(UiState.Content::class.java)

            connection.setState(ConnectionState.Disconnected(willRetry = true))
            expectNoEvents()
        }
    }

    @Test
    fun `unauthorized connection state maps to Unauthorized`() = runTest(testDispatcher) {
        val connection = FakeConnectionSource(initialApi = null)
        val viewModel = DashboardViewModel(connection)

        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(UiState.Loading::class.java)
            connection.setState(ConnectionState.Unauthorized)
            assertThat(awaitItem()).isInstanceOf(UiState.Unauthorized::class.java)
        }
    }
}
