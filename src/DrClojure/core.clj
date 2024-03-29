(ns DrClojure.core
  (:gen-class)
  (:import
    (javax.swing JFrame SwingUtilities JPanel BoxLayout JMenuBar JMenu JMenuItem KeyStroke JButton JTextArea JTextField JScrollPane)
    (java.awt BorderLayout Font)
    (java.awt.event ActionEvent ActionListener)
    (java.io Writer)))

(defn -main []
  (def title "DrClojure")
  
  (def aCurFileName (atom ""))
  
  (. javax.swing.UIManager setLookAndFeel 
    (. javax.swing.UIManager getSystemLookAndFeelClassName)) ; make look native
  
  (def frame (new JFrame))
  (defn updateFileName [fileName]
    (reset! aCurFileName fileName)
    (. frame setTitle (str (deref aCurFileName) " - " title)))
  
  (updateFileName "")
  
  (def text (new JTextArea 20 80))
  (def pane (new JScrollPane text))
  (def textf (new JTextField))
  (def button (new JButton "Run"))
  (. button setMnemonic \r)
  
  (defn eval-code [code]
    (try (. clojure.lang.Compiler load (new java.io.StringReader (str "(ns user) " code)))
      (catch Exception e e)))
  
  (. button addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (println (eval-code (. text getText))))))
  
  (defn eval-print [code]
    (println (str "> " code "\n" (eval-code code))))
  
  (. textf addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (eval-print (. textf getText))
        (.setText textf nil))))
  
  (def fc (new javax.swing.JFileChooser))
  (. fc setFileFilter (new javax.swing.filechooser.FileNameExtensionFilter "Clojure file (*.clj)" (into-array ["clj"])))
  
  (def panel (new JPanel))
  
  (def menuBar (new JMenuBar))
  (def menuFile (new JMenu "File"))
  (.setMnemonic menuFile (int \f))
  (def menuNew (new JMenuItem "New" (int \n)))
  (def menuOpen (new JMenuItem "Open..." (int \o)))
  (def menuSave (new JMenuItem "Save" (int \s)))  
  (def menuSaveAs (new JMenuItem "Save As..." (int \a)))
  (.setAccelerator menuNew (KeyStroke/getKeyStroke "control N"))
  (.setAccelerator menuOpen (KeyStroke/getKeyStroke "control O"))
  (.setAccelerator menuSave (KeyStroke/getKeyStroke "control S"))
  (.setAccelerator menuSaveAs (KeyStroke/getKeyStroke "control alt S"))  
  (def menuExit (new JMenuItem "Exit" (int \x)))
  (. menuBar add menuFile)
  (. menuFile add menuNew)
  (. menuFile add menuOpen)
  (. menuFile add menuSave)
  (. menuFile add menuSaveAs)
  (. menuFile addSeparator)
  (. menuFile add menuExit)
  (. frame setJMenuBar menuBar)
  
  (. menuNew addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (updateFileName "")        
        (. text setText ""))))
  
  (. menuOpen addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (when (= (. fc showOpenDialog nil) javax.swing.JFileChooser/APPROVE_OPTION)
          (let [fileName (.. fc getSelectedFile getCanonicalPath)]
            (updateFileName fileName)
            (. text setText (slurp fileName)))))))
  
  (defn fileSave [fileName]
    (spit fileName (. text getText)))
  
  (defn fileSaveAs []    
    (when (= (. fc showSaveDialog nil) javax.swing.JFileChooser/APPROVE_OPTION)
      (let [fileName (.. fc getSelectedFile getCanonicalPath)]
        (updateFileName fileName)
        (fileSave fileName))))
  
  (. menuSave addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]      
        (if (= (deref aCurFileName) "")
          (fileSaveAs) 
          (fileSave @aCurFileName)))))
  
  (. menuSaveAs addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (fileSaveAs))))
  
  (. menuExit addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (System/exit 0))))
  
  (. frame setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
  (let [font (new Font "Consolas" Font/PLAIN 15)]
    (. text setFont font)
    (. textf setFont font))
  (. panel setLayout (new BoxLayout panel BoxLayout/PAGE_AXIS))
  (. frame add button BorderLayout/NORTH)
  (. frame add pane)
  (. panel add textf)
  (. frame add panel BorderLayout/SOUTH)
  (. frame pack)
  (. text requestFocus)
  (. frame setVisible true)
  (doseq [s ["(clojure-version)" "(use 'clojure.repl)"]] (eval-print s)))
