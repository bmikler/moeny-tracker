package com.android.moneytracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.android.moneytracker.R
import com.android.moneytracker.ui.navigation.MoneyTrackerNavHost

@Composable
fun MoneyTrackerApp(
    navController: NavHostController = rememberNavController()
) {
    Column {
        MoneyTrackerNavHost(navController = navController)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyTrackerTopAppBar(
    title: String = stringResource(R.string.app_name),
    canNavigateBack: Boolean = false,
    navigateBack: () -> Unit = {},
    isActionEnable: Boolean = false,
    onActionButton: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        actions = {
            if (isActionEnable) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.btn_settings)
                    )
                }
            }

        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.btn_back)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    MoneyTrackerTopAppBar(canNavigateBack = true)
}
