package cz.applifting.graphqlempty.navigation.navDrawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cz.applifting.graphqlempty.R

@Composable
fun DrawerHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 150.dp)
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            fontWeight= FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
    }
}

@Composable
fun DrawerItem(
    item: NavDrawerItem,
    onItemClicked: (NavDrawerItem) -> Unit
) {
    val mod = Modifier
        .height(56.dp)
        .padding(vertical = 8.dp, horizontal = 8.dp)
        .clip(RoundedCornerShape(4.dp))
        .clickable { onItemClicked(item) }
        .fillMaxWidth()

    var rowMod = Modifier.padding()
    var tint = MaterialTheme.colors.onSurface
    if (item.isSelected) {
        rowMod = rowMod.background(color = MaterialTheme.colors.primary.copy(alpha = 0.15f))
        tint = MaterialTheme.colors.primary
    }

    Surface(modifier = mod) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = rowMod
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = item.icon, contentDescription = null, tint = tint)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = item.title),
                style = MaterialTheme.typography.subtitle1,
                color = tint
            )
        }
    }
}

@Composable
fun AppDrawer(
    drawerItems: List<NavDrawerItem>,
    onItemClicked: (NavDrawerItem)->Unit
) {
    Column {
        DrawerHeader()
        LazyColumn() {
            items(drawerItems) { item ->
                DrawerItem(item = item, onItemClicked = onItemClicked)
            }
        }
    }
}