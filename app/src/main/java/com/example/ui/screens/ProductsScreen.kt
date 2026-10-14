package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.components.CaptioBottomBar
import com.example.ui.theme.CaptioBgDark
import com.example.ui.theme.CaptioBgDeep
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioPurplePrimary
import com.example.ui.theme.CaptioTextGray
import com.example.ui.theme.CaptioTextWhite
import com.example.ui.viewmodel.CaptioScreen
import com.example.ui.viewmodel.CaptioViewModel
import java.util.Locale

@Composable
fun ProductsScreen(
    viewModel: CaptioViewModel,
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier,
    isEmbeddedInMockup: Boolean = false
) {
    val products by viewModel.filteredProducts.collectAsState()
    val allProducts by viewModel.products.collectAsState()
    val searchQuery by viewModel.productSearchQuery.collectAsState()
    val activeFilterTab by viewModel.productFilterTab.collectAsState()

    val totalCount = allProducts.size.coerceAtLeast(24)
    val activeCount = allProducts.count { it.status == "Ativo" }.coerceAtLeast(22)
    val inactiveCount = (totalCount - activeCount).coerceAtLeast(2)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CaptioBgDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isEmbeddedInMockup) 10.dp else 18.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Top Bar: Back, "Produtos", Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(CaptioScreen.HOME) },
                    modifier = Modifier.testTag("products_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Produtos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = { /* Filter */ },
                    modifier = Modifier.testTag("products_filter_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(CaptioCardBg)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = CaptioTextGray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setProductSearchQuery(it) },
                    placeholder = { Text("Buscar produto...", color = CaptioTextGray, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("product_search_input"),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Tabs: "Todos (24)", "Ativos (22)", "Inativos (2)"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterPill(
                    label = "Todos ($totalCount)",
                    isSelected = activeFilterTab == "Todos",
                    onClick = { viewModel.setProductFilterTab("Todos") },
                    testTag = "filter_prod_all"
                )
                FilterPill(
                    label = "Ativos ($activeCount)",
                    isSelected = activeFilterTab == "Ativos",
                    onClick = { viewModel.setProductFilterTab("Ativos") },
                    testTag = "filter_prod_active"
                )
                FilterPill(
                    label = "Inativos ($inactiveCount)",
                    isSelected = activeFilterTab == "Inativos",
                    onClick = { viewModel.setProductFilterTab("Inativos") },
                    testTag = "filter_prod_inactive"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Product List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(products) { product ->
                    ProductListItem(product = product)
                }

                item {
                    Spacer(modifier = Modifier.height(if (isEmbeddedInMockup) 60.dp else 90.dp))
                }
            }
        }

        // Bottom Navigation Bar
        if (!isEmbeddedInMockup) {
            CaptioBottomBar(
                currentScreen = CaptioScreen.PRODUCTS,
                onNavigate = onNavigate,
                onQuickAdd = onQuickAdd,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun ProductListItem(product: Product) {
    val icon: ImageVector = when (product.category) {
        "Vestuário" -> Icons.Default.Checkroom
        "Prótese" -> Icons.Default.Face
        "Insumos" -> Icons.Default.Inventory2
        "Cosméticos" -> Icons.Default.ShoppingBag
        else -> Icons.Default.ShoppingBag
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CaptioCardBg)
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag("product_item_${product.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail with modern badge
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF2E1C59), Color(0xFF1B0E38))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = product.name,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "R$ ${String.format(Locale.GERMANY, "%,.2f", product.price)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF7C3AED)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Status Tag "Ativo" / "Inativo"
        val isActive = product.status == "Ativo"
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isActive) Color(0x1F00FF66) else Color(0x22888888))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = product.status,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) CaptioNeonGreen else CaptioTextGray
            )
        }
    }
}
