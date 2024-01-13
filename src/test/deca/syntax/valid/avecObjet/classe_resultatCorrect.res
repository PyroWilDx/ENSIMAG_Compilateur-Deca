`> [1, 0] Program
   +> ListDeclClass [List with 2 elements]
   |  []> [1, 0] DeclClass
   |  ||  +> [1, 6] Identifier (A)
   |  ||  +> [1, 8] Identifier (Object)
   |  ||  +> ListDeclField [List with 1 elements]
   |  ||  |  []> [2, 8] DeclField
   |  ||  |      PUBLIC
   |  ||  |      +> [2, 4] Identifier (int)
   |  ||  |      +> [2, 8] Identifier (a)
   |  ||  |      `> NoInitialization
   |  ||  `> ListDeclMethod [List with 1 elements]
   |  ||     []> [3, 4] DeclMethod
   |  ||         +> [3, 4] Identifier (int)
   |  ||         +> [3, 8] Identifier (summ)
   |  ||         +> [3, 13] ListDeclParam [List with 2 elements]
   |  ||         |  []> [3, 13] DeclParam
   |  ||         |  ||  +> [3, 13] Identifier (int)
   |  ||         |  ||  `> [3, 17] Identifier (b)
   |  ||         |  []> [3, 19] DeclParam
   |  ||         |      +> [3, 19] Identifier (int)
   |  ||         |      `> [3, 23] Identifier (c)
   |  ||         +> ListDeclVar [List with 0 elements]
   |  ||         `> ListInst [List with 2 elements]
   |  ||            []> [4, 8] Print
   |  ||            ||  `> ListExpr [List with 1 elements]
   |  ||            ||     []> [4, 14] Plus
   |  ||            ||         +> [4, 14] Identifier (b)
   |  ||            ||         `> [4, 16] Int (1)
   |  ||            []> [5, 8] Return
   |  ||                `> [5, 15] Identifier (a)
   |  []> [8, 0] DeclClass
   |      +> [8, 6] Identifier (B)
   |      +> [8, 8] Identifier (A)
   |      +> ListDeclField [List with 1 elements]
   |      |  []> [9, 8] DeclField
   |      |      PUBLIC
   |      |      +> [9, 4] Identifier (int)
   |      |      +> [9, 8] Identifier (b)
   |      |      `> NoInitialization
   |      `> ListDeclMethod [List with 0 elements]
   `> [11, 0] Main
      +> ListDeclVar [List with 1 elements]
      |  []> [12, 6] DeclVar
      |      +> [12, 4] Identifier (A)
      |      +> [12, 6] Identifier (anass)
      |      `> [12, 12] Initialization
      |         `> [12, 14] New
      |            `> [12, 18] Identifier (A)
      `> ListInst [List with 2 elements]
         []> [13, 4] Assign
         ||  +> [13, 4] FieldSelection
         ||  |  +> [13, 4] Identifier (anass)
         ||  |  `> [13, 10] Identifier (a)
         ||  `> [13, 14] Int (1)
         []> [14, 4] MethodCall
             +> [14, 4] Identifier (anass)
             +> [14, 10] Identifier (summ)
             `> [14, 15] RValueStar [List with 2 elements]
                []> [14, 15] Identifier (a)
                []> [14, 18] Identifier (b)
