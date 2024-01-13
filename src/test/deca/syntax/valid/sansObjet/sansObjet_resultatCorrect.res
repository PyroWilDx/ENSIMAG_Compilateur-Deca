`> [11, 0] Program
   +> ListDeclClass [List with 0 elements]
   `> [11, 0] Main
      +> ListDeclVar [List with 2 elements]
      |  []> [12, 4] DeclVar
      |  ||  +> [12, 0] Identifier (int)
      |  ||  +> [12, 4] Identifier (i)
      |  ||  `> [12, 6] Initialization
      |  ||     `> [12, 8] Int (3)
      |  []> [13, 8] DeclVar
      |      +> [13, 0] Identifier (boolean)
      |      +> [13, 8] Identifier (a)
      |      `> [13, 10] Initialization
      |         `> [13, 12] BooleanLiteral (true)
      `> ListInst [List with 1 elements]
         []> [14, 0] While
             +> [14, 6] And
             |  +> [14, 6] Greater
             |  |  +> [14, 6] Identifier (i)
             |  |  `> [14, 8] Int (0)
             |  `> [14, 13] Identifier (a)
             `> ListInst [List with 2 elements]
                []> [15, 4] IfThenElse
                ||  +> [15, 7] Equals
                ||  |  +> [15, 7] Identifier (i)
                ||  |  `> [15, 12] Int (3)
                ||  +> ListInst [List with 1 elements]
                ||  |  []> [16, 8] Print
                ||  |      `> ListExpr [List with 1 elements]
                ||  |         []> [16, 14] StringLiteral ("if")
                ||  `> ListInst [List with 1 elements]
                ||     []> [18, 12] IfThenElse
                ||         +> [18, 12] Equals
                ||         |  +> [18, 12] Identifier (i)
                ||         |  `> [18, 16] Int (2)
                ||         +> ListInst [List with 1 elements]
                ||         |  []> [19, 12] Print
                ||         |      `> ListExpr [List with 1 elements]
                ||         |         []> [19, 18] StringLiteral ("elseIf")
                ||         `> ListInst [List with 1 elements]
                ||            []> [22, 9] Print
                ||                `> ListExpr [List with 1 elements]
                ||                   []> [22, 15] StringLiteral ("else")
                []> [24, 4] Assign
                    +> [24, 4] Identifier (i)
                    `> [24, 8] Minus
                       +> [24, 8] Identifier (i)
                       `> [24, 10] Int (1)
