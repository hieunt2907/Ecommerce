src/main/java/com/ecommerce/
├── config/                # Các lớp cấu hình
├── exception/             # Xử lý exception
├── common/                # Các thành phần dùng chung
│   ├── dto/               # Các DTO dùng chung
│   ├── utils/             # Các tiện ích
│   └── constants/         # Các hằng số
├── security/              # Xác thực và phân quyền
└── features/              # Các feature chính (tổ chức theo domain)
    ├── auth/              # Xác thực người dùng
    ├── users/             # Quản lý người dùng 
    ├── shops/             # Quản lý cửa hàng
    ├── categories/        # Quản lý danh mục
    ├── products/          # Quản lý sản phẩm
    ├── variants/          # Quản lý biến thể sản phẩm
    ├── inventory/         # Quản lý kho hàng
    ├── orders/            # Quản lý đơn hàng
    ├── payments/          # Quản lý thanh toán
    ├── reviews/           # Quản lý đánh giá
    ├── cart/              # Quản lý giỏ hàng
    └── promotions/        # Quản lý khuyến mãi