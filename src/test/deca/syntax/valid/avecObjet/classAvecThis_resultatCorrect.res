`> [1, 0] Program
   +> ListDeclClass [List with 1 elements]
   |  []> [1, 0] DeclClass
   |      +> [1, 6] Identifier (A)
   |      +> [1, 8] Identifier (Object)
   |      +> ListDeclField [List with 1 elements]
   |      |  []> [2, 8] DeclField
   |      |      PUBLIC
   |      |      +> [2, 4] Identifier (int)
   |      |      +> [2, 8] Identifier (a)
   |      |      `> Initialization
   |      |         `> [2, 12] Int (2)
   |      `> ListDeclMethod [List with 2 elements]
   |         []> [3, 4] DeclMethod
   |         ||  +> [3, 4] Identifier (int)
   |         ||  +> [3, 8] Identifier (summ)
   |         ||  +> [3, 13] ListDeclParam [List with 1 elements]
   |         ||  |  []> [3, 13] DeclParam
   |         ||  |      +> [3, 13] Identifier (int)
   |         ||  |      `> [3, 17] Identifier (b)
   |         ||  +> ListDeclVar [List with 0 elements]
   |         ||  `> ListInst [List with 1 elements]
   |         ||     []> [4, 8] Return
   |         ||         `> [4, 15] FieldSelection
   |         ||            +> [4, 15] This
   |         ||            `> [4, 20] Identifier (a)
   |         []> [6, 4] DeclMethod
   |             +> [6, 4] Identifier (int)
   |             +> [6, 8] Identifier (summ2)
   |             +> [6, 14] ListDeclParam [List with 0 elements]
   |             +> ListDeclVar [List with 0 elements]
   |             `> ListInst [List with 1 elements]
   |                []> [7, 8] Return
   |                    `> [7, 15] MethodCall
   |                       +> [7, 15] This
   |                       +> [7, 20] Identifier (summ)
   |                       `> [7, 25] RValueStar [List with 1 elements]
   |                          []> [7, 25] Int (5)
   `> [10, 0] Main
      +> ListDeclVar [List with 3 elements]
      |  []> [11, 6] DeclVar
      |  ||  +> [11, 4] Identifier (A)
      |  ||  +> [11, 6] Identifier (anass)
      |  ||  `> [11, 12] Initialization
      |  ||     `> [11, 14] New
      |  ||        `> [11, 18] Identifier (A)
      |  []> [12, 8] DeclVar
      |  ||  +> [12, 4] Identifier (int)
      |  ||  +> [12, 8] Identifier (c)
      |  ||  `> [12, 10] Initialization
      |  ||     `> [12, 12] MethodCall
      |  ||        +> [12, 12] Identifier (anass)
      |  ||        +> [12, 18] Identifier (summ)
      |  ||        `> [12, 23] RValueStar [List with 1 elements]
      |  ||           []> [12, 23] Int (5)
      |  []> [13, 8] DeclVar
      |      +> [13, 4] Identifier (int)
      |      +> [13, 8] Identifier (d)
      |      `> [13, 10] Initialization
      |         `> [13, 12] MethodCall
      |            +> [13, 12] Identifier (anass)
      |            +> [13, 18] Identifier (summ2)
      |            `> [13, 24] RValueStar [List with 0 elements]
      `> ListInst [List with 0 elements]
